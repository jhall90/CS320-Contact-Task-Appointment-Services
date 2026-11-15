package projectone.enhanced;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import projectone.enhanced.appointment.AppointmentRepository;
import projectone.enhanced.contact.ContactRepository;
import projectone.enhanced.task.TaskRepository;

/*
 * End-to-end tests through the real security filter chain:
 * register, log in, call the API with a JWT, and check status codes.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "app.security.jwt-secret=test-secret-that-is-at-least-32-characters-long",
        "app.security.admin-username=admin",
        "app.security.admin-password=AdminPass123"
})
class ApiIntegrationTest {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private ContactRepository contacts;
    @Autowired private TaskRepository tasks;
    @Autowired private AppointmentRepository appointments;

    @BeforeEach
    void clearData() {
        contacts.clear();
        tasks.clear();
        appointments.clear();
    }
    
    // -----------------------------
    // ---------- HELPERS ----------
    // -----------------------------

    private String newUserToken() throws Exception {
        String username = "user" + COUNTER.incrementAndGet();
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roles[0]").value("USER"));
        return login(username, "password123");
    }

    private String login(String username, String password) throws Exception {
        String body = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode json = mapper.readTree(body);
        return json.get("accessToken").asText();
    }

    private static MockHttpServletRequestBuilder auth(MockHttpServletRequestBuilder req, String token) {
        return req.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON);
    }

    private static final String CONTACT_JSON =
            "{\"contactId\":\"C1\",\"firstName\":\"Josh\",\"lastName\":\"Hall\","
            + "\"phone\":\"6035551234\",\"address\":\"1 Main St\"}";

    
    // ------------------------------------
    // ---------- AUTHENTICATION ----------
    // ------------------------------------
    
    @Test
    void requestsWithoutTokenAreRejected() throws Exception {
        mvc.perform(get("/api/contacts")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/tasks")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/appointments")).andExpect(status().isUnauthorized());
    }

    @Test
    void tamperedTokenIsRejected() throws Exception {
        String token = newUserToken();
        String tampered = token.substring(0, token.length() - 2) + "xx";
        mvc.perform(auth(get("/api/contacts"), tampered)).andExpect(status().isUnauthorized());
    }

    @Test
    void wrongPasswordAndUnknownUserGiveSameError() throws Exception {
        newUserToken();
        String user = "user" + COUNTER.get();
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + user + "\",\"password\":\"wrongpass1\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Invalid username or password"));
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"nobody\",\"password\":\"wrongpass1\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Invalid username or password"));
    }

    @Test
    void duplicateUsernameIsRejectedCaseInsensitively() throws Exception {
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Admin\",\"password\":\"password123\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void weakRegistrationIsRejected() throws Exception {
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"ab\",\"password\":\"short\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.username").exists())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    // ------------------------------------
    // ---- CONTACTS CRUD + Validation ----
    // ------------------------------------

    @Test
    void contactCrudFlow() throws Exception {
        String token = newUserToken();

        mvc.perform(auth(post("/api/contacts"), token).content(CONTACT_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contactId").value("C1"))
                .andExpect(jsonPath("$.owner").doesNotExist());

        mvc.perform(auth(patch("/api/contacts/C1"), token).content("{\"phone\":\"6035559999\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("6035559999"))
                .andExpect(jsonPath("$.firstName").value("Josh"));

        mvc.perform(auth(get("/api/contacts"), token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mvc.perform(auth(delete("/api/contacts/C1"), token)).andExpect(status().isNoContent());
        mvc.perform(auth(get("/api/contacts/C1"), token)).andExpect(status().isNotFound());
    }

    @Test
    void invalidContactReturnsFieldErrors() throws Exception {
        String token = newUserToken();
        mvc.perform(auth(post("/api/contacts"), token).content(
                "{\"contactId\":\"12345678901\",\"firstName\":\"Josh\",\"lastName\":\"Hall\","
                + "\"phone\":\"123\",\"address\":\"1 Main St\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.errors.contactId").exists())
                .andExpect(jsonPath("$.errors.phone").value("must be exactly 10 digits"));
    }

    @Test
    void malformedJsonIsBadRequest() throws Exception {
        String token = newUserToken();
        mvc.perform(auth(post("/api/contacts"), token).content("{not json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void duplicateContactIdIsConflict() throws Exception {
        String token = newUserToken();
        mvc.perform(auth(post("/api/contacts"), token).content(CONTACT_JSON)).andExpect(status().isCreated());
        mvc.perform(auth(post("/api/contacts"), token).content(CONTACT_JSON)).andExpect(status().isConflict());
    }

    @Test
    void overlongPathIdIsBadRequest() throws Exception {
        String token = newUserToken();
        mvc.perform(auth(get("/api/contacts/12345678901"), token)).andExpect(status().isBadRequest());
    }

    // ------------------------------------
    // ------- OWNERSHIP AND ROLES --------
    // ------------------------------------

    @Test
    void usersCannotReachEachOthersRecords() throws Exception {
        String alice = newUserToken();
        String bob = newUserToken();
        mvc.perform(auth(post("/api/contacts"), alice).content(CONTACT_JSON)).andExpect(status().isCreated());

        mvc.perform(auth(get("/api/contacts/C1"), bob)).andExpect(status().isNotFound());
        mvc.perform(auth(patch("/api/contacts/C1"), bob).content("{\"firstName\":\"Hacked\"}"))
                .andExpect(status().isNotFound());
        mvc.perform(auth(delete("/api/contacts/C1"), bob)).andExpect(status().isNotFound());
        mvc.perform(auth(get("/api/contacts"), bob)).andExpect(jsonPath("$", hasSize(0)));

        // Alice's record is untouched
        mvc.perform(auth(get("/api/contacts/C1"), alice)).andExpect(jsonPath("$.firstName").value("Josh"));
    }

    @Test
    void adminCanSeeAllRecordsAndUsers() throws Exception {
        String alice = newUserToken();
        mvc.perform(auth(post("/api/contacts"), alice).content(CONTACT_JSON)).andExpect(status().isCreated());

        String admin = login("admin", "AdminPass123");
        mvc.perform(auth(get("/api/contacts/C1"), admin)).andExpect(status().isOk());
        mvc.perform(auth(get("/api/admin/users"), admin)).andExpect(status().isOk());
    }

    @Test
    void regularUserCannotUseAdminEndpoints() throws Exception {
        String token = newUserToken();
        mvc.perform(auth(get("/api/admin/users"), token)).andExpect(status().isForbidden());
    }

    // ------------------------------------
    // ----- TASKS AND APPOINTMENTS -------
    // ------------------------------------

    @Test
    void taskFlow() throws Exception {
        String token = newUserToken();
        mvc.perform(auth(post("/api/tasks"), token)
                .content("{\"taskId\":\"T1\",\"name\":\"Milestone Two\",\"description\":\"Enhance CS 320\"}"))
                .andExpect(status().isCreated());
        mvc.perform(auth(patch("/api/tasks/T1"), token).content("{\"name\":\"Done\"}"))
                .andExpect(jsonPath("$.name").value("Done"))
                .andExpect(jsonPath("$.description").value("Enhance CS 320"));
        mvc.perform(auth(post("/api/tasks"), token)
                .content("{\"taskId\":\"T2\",\"name\":\"" + "x".repeat(21) + "\",\"description\":\"d\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());
    }

    @Test
    void appointmentFlow() throws Exception {
        String token = newUserToken();
        String future = LocalDateTime.now().plusDays(30).withNano(0).toString();
        String past = LocalDateTime.now().minusDays(1).withNano(0).toString();

        mvc.perform(auth(post("/api/appointments"), token)
                .content("{\"appointmentId\":\"A1\",\"appointmentDate\":\"" + future
                        + "\",\"description\":\"Dentist\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.appointmentDate").exists());

        mvc.perform(auth(post("/api/appointments"), token)
                .content("{\"appointmentId\":\"A2\",\"appointmentDate\":\"" + past
                        + "\",\"description\":\"Too late\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.appointmentDate").value("cannot be in the past"));

        mvc.perform(auth(patch("/api/appointments/A1"), token)
                .content("{\"appointmentDate\":\"" + past + "\"}"))
                .andExpect(status().isBadRequest());

        mvc.perform(auth(delete("/api/appointments/A1"), token)).andExpect(status().isNoContent());
    }
}

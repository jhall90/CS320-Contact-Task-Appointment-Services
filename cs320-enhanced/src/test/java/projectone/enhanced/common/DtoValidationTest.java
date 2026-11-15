package projectone.enhanced.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import projectone.enhanced.appointment.AppointmentDtos.CreateAppointmentRequest;
import projectone.enhanced.appointment.AppointmentDtos.UpdateAppointmentRequest;
import projectone.enhanced.auth.AuthDtos.RegisterRequest;
import projectone.enhanced.contact.ContactDtos.CreateContactRequest;
import projectone.enhanced.contact.ContactDtos.UpdateContactRequest;
import projectone.enhanced.task.TaskDtos.CreateTaskRequest;

/*
 * Boundary tests for the original CS 320 field rules, now enforced byBean Validation on the request DTOs.
 */
class DtoValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    private Set<String> badFields(Object dto) {
        return validator.validate(dto).stream()
                .map(v -> v.getPropertyPath().toString())
                .collect(Collectors.toSet());
    }

    @Test
    void validContactPasses() {
        assertTrue(badFields(new CreateContactRequest("1234567890", "Josh", "Hall",
                "6035551234", "123 Main St")).isEmpty());
    }

    @Test
    void contactFieldsAtLimitPlusOneFail() {
        Set<String> bad = badFields(new CreateContactRequest("12345678901", "ABCDEFGHIJK",
                "ABCDEFGHIJK", "603555123", "1234567890123456789012345678901"));
        assertEquals(Set.of("contactId", "firstName", "lastName", "phone", "address"), bad);
    }

    @Test
    void contactNullAndBlankFieldsFail() {
        assertEquals(5, badFields(new CreateContactRequest(null, "", " ", null, null)).size());
    }

    @Test
    void phoneMustBeDigitsOnly() {
        assertEquals(Set.of("phone"), badFields(new CreateContactRequest("1", "A", "B",
                "60355512a4", "Addr")));
    }

    @Test
    void contactUpdateAllowsNullsButNotEmptyStrings() {
        assertTrue(badFields(new UpdateContactRequest(null, null, null, null)).isEmpty());
        assertEquals(Set.of("firstName"), badFields(new UpdateContactRequest("", null, null, null)));
    }

    @Test
    void taskRules() {
        assertTrue(badFields(new CreateTaskRequest("1", "12345678901234567890",
                "x".repeat(50))).isEmpty());
        assertEquals(Set.of("taskId", "name", "description"), badFields(new CreateTaskRequest(
                "12345678901", "123456789012345678901", "x".repeat(51))));
    }

    @Test
    void appointmentDateCannotBeInThePast() {
        assertEquals(Set.of("appointmentDate"), badFields(new CreateAppointmentRequest("1",
                LocalDateTime.now().minusDays(1), "Checkup")));
        assertTrue(badFields(new CreateAppointmentRequest("1",
                LocalDateTime.now().plusDays(1), "Checkup")).isEmpty());
        assertEquals(Set.of("appointmentDate"), badFields(new UpdateAppointmentRequest(
                LocalDateTime.now().minusMinutes(5), null)));
    }

    @Test
    void appointmentRequiresDate() {
        assertEquals(Set.of("appointmentDate"), badFields(new CreateAppointmentRequest("1",
                null, "Checkup")));
    }

    @Test
    void registrationRules() {
        assertTrue(badFields(new RegisterRequest("josh_h", "password123")).isEmpty());
        assertEquals(Set.of("username", "password"),
                badFields(new RegisterRequest("j!", "short")));
        assertEquals(Set.of("password"),
                badFields(new RegisterRequest("josh", "x".repeat(73))));
    }
}

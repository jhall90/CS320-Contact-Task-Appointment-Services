package projectone.enhanced.contact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import projectone.enhanced.common.CurrentUser;
import projectone.enhanced.common.DuplicateResourceException;
import projectone.enhanced.common.ResourceNotFoundException;
import projectone.enhanced.contact.Contact;
import projectone.enhanced.contact.ContactRepository;
import projectone.enhanced.contact.ContactService;
import projectone.enhanced.contact.ContactDtos.CreateContactRequest;
import projectone.enhanced.contact.ContactDtos.UpdateContactRequest;

class ContactServiceTest {

    private static final CurrentUser ALICE = new CurrentUser("alice", false);
    private static final CurrentUser BOB = new CurrentUser("bob", false);
    private static final CurrentUser ADMIN = new CurrentUser("admin", true);

    private ContactService service;

    @BeforeEach
    void setUp() {
        service = new ContactService(new ContactRepository());
        service.create(new CreateContactRequest("C1", "Josh", "Hall", "6035551234", "1 Main St"), ALICE);
    }

    @Test
    void createSetsOwnerFromCaller() {
        assertEquals("alice", service.get("C1", ALICE).getOwner());
    }

    @Test
    void duplicateIdRejected() {
        assertThrows(DuplicateResourceException.class, () -> service.create(
                new CreateContactRequest("C1", "A", "B", "6035550000", "Addr"), BOB));
    }

    @Test
    void partialUpdateOnlyChangesProvidedFields() {
        Contact updated = service.update("C1", new UpdateContactRequest(null, "Smith", null, null), ALICE);
        assertEquals("Josh", updated.getFirstName());
        assertEquals("Smith", updated.getLastName());
        assertEquals("6035551234", updated.getPhone());
    }

    @Test
    void updateAllFields() {
        Contact c = service.update("C1",
                new UpdateContactRequest("Kirsten", "Arnold", "6035559999", "2 Elm St"), ALICE);
        assertEquals("Kirsten", c.getFirstName());
        assertEquals("6035559999", c.getPhone());
        assertEquals("2 Elm St", c.getAddress());
    }

    @Test
    void otherUserCannotSeeUpdateOrDelete() {
        assertThrows(ResourceNotFoundException.class, () -> service.get("C1", BOB));
        assertThrows(ResourceNotFoundException.class,
                () -> service.update("C1", new UpdateContactRequest("X", null, null, null), BOB));
        assertThrows(ResourceNotFoundException.class, () -> service.delete("C1", BOB));
        assertTrue(service.list(BOB).isEmpty());
    }

    @Test
    void adminCanSeeEveryone() {
        service.create(new CreateContactRequest("C2", "Bo", "B", "6035550000", "Addr"), BOB);
        assertEquals(2, service.list(ADMIN).size());
        assertEquals(1, service.list(ALICE).size());
        assertEquals("Josh", service.get("C1", ADMIN).getFirstName());
    }

    @Test
    void deleteThenGetIsNotFound() {
        service.delete("C1", ALICE);
        assertThrows(ResourceNotFoundException.class, () -> service.get("C1", ALICE));
        assertThrows(ResourceNotFoundException.class, () -> service.delete("C1", ALICE));
    }
}

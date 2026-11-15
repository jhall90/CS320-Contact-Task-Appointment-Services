package projectone.enhanced.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import projectone.enhanced.appointment.Appointment;
import projectone.enhanced.appointment.AppointmentRepository;
import projectone.enhanced.appointment.AppointmentService;
import projectone.enhanced.appointment.AppointmentDtos.CreateAppointmentRequest;
import projectone.enhanced.appointment.AppointmentDtos.UpdateAppointmentRequest;
import projectone.enhanced.common.CurrentUser;
import projectone.enhanced.common.DuplicateResourceException;
import projectone.enhanced.common.ResourceNotFoundException;

class AppointmentServiceTest {

    private static final CurrentUser ALICE = new CurrentUser("alice", false);
    private static final CurrentUser BOB = new CurrentUser("bob", false);
    private static final LocalDateTime FUTURE = LocalDateTime.now().plusDays(10).withNano(0);

    private AppointmentService service;

    @BeforeEach
    void setUp() {
        service = new AppointmentService(new AppointmentRepository());
        service.create(new CreateAppointmentRequest("A1", FUTURE, "Dentist"), ALICE);
    }

    @Test
    void createAndGet() {
        Appointment a = service.get("A1", ALICE);
        assertEquals(FUTURE, a.getAppointmentDate());
        assertEquals("Dentist", a.getDescription());
    }

    @Test
    void duplicateRejected() {
        assertThrows(DuplicateResourceException.class,
                () -> service.create(new CreateAppointmentRequest("A1", FUTURE, "Other"), BOB));
    }

    @Test
    void partialUpdate() {
        LocalDateTime later = FUTURE.plusDays(5);
        Appointment a = service.update("A1", new UpdateAppointmentRequest(later, null), ALICE);
        assertEquals(later, a.getAppointmentDate());
        assertEquals("Dentist", a.getDescription());
        a = service.update("A1", new UpdateAppointmentRequest(null, "Cleaning"), ALICE);
        assertEquals("Cleaning", a.getDescription());
    }

    @Test
    void ownershipEnforced() {
        assertThrows(ResourceNotFoundException.class, () -> service.get("A1", BOB));
        assertEquals(0, service.list(BOB).size());
    }

    @Test
    void delete() {
        service.delete("A1", ALICE);
        assertThrows(ResourceNotFoundException.class, () -> service.get("A1", ALICE));
    }
}

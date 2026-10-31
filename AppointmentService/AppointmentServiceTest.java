package projectone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.Calendar;

/**
 * Unit tests for AppointmentService class.
 */
public class AppointmentServiceTest {

    private AppointmentService service;

    private Date futureDate(int daysAhead) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, daysAhead);
        return cal.getTime();
    }

    @BeforeEach
    void setUp() {
        service = new AppointmentService();
    }

    @Test
    void addAppointmentStoresUniqueAppointments() {
        Appointment a1 = new Appointment("1", futureDate(3), "Meeting");
        Appointment a2 = new Appointment("2", futureDate(5), "Dentist Visit");

        service.addAppointment(a1);
        service.addAppointment(a2);
        assertEquals(2, service.getAllAppointments().size());

        // Duplicate ID should throw
        Appointment dup = new Appointment("1", futureDate(7), "Duplicate");
        assertThrows(IllegalArgumentException.class, () -> service.addAppointment(dup));
    }

    @Test
    void deleteAppointmentRemovesById() {
        Appointment a = new Appointment("1", futureDate(4), "Interview");
        service.addAppointment(a);

        assertTrue(service.deleteAppointment("1"));
        assertFalse(service.deleteAppointment("1")); // Already deleted
    }

    @Test
    void updateAppointmentFields() {
        Appointment a = new Appointment("9", futureDate(3), "Initial Description");
        service.addAppointment(a);

        Date newDate = futureDate(7);
        service.updateAppointmentDate("9", newDate);
        service.updateAppointmentDescription("9", "Updated Description");

        Appointment updated = service.getAllAppointments().get("9");
        assertEquals(newDate, updated.getAppointmentDate());
        assertEquals("Updated Description", updated.getDescription());
    }

    @Test
    void updateNonexistentAppointmentThrows() {
        assertThrows(IllegalArgumentException.class, () -> service.updateAppointmentDate("missing", futureDate(2)));
        assertThrows(IllegalArgumentException.class, () -> service.updateAppointmentDescription("missing", "Nope"));
    }
}

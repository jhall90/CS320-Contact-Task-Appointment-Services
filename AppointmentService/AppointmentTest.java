package projectone;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.Calendar;

/**
 * Unit tests for Appointment class.
 */
public class AppointmentTest {

    // Helper to create future dates
    private Date futureDate(int daysAhead) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, daysAhead);
        return cal.getTime();
    }

    @Test
    void constructorValidInputCreatesAppointment() {
        Appointment a = new Appointment("A1", futureDate(5), "Dentist Appointment");
        assertEquals("A1", a.getAppointmentId());
        assertEquals("Dentist Appointment", a.getDescription());
        assertNotNull(a.getAppointmentDate());
    }

    @Test
    void constructorRejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> new Appointment(null, futureDate(2), "Description"));
        assertThrows(IllegalArgumentException.class, () -> new Appointment("12345678901", futureDate(2), "Description"));
        assertThrows(IllegalArgumentException.class, () -> new Appointment("A1", null, "Description"));
        assertThrows(IllegalArgumentException.class, () -> new Appointment("A1", new Date(System.currentTimeMillis() - 86400000), "Description"));
        assertThrows(IllegalArgumentException.class, () -> new Appointment("A1", futureDate(2), null));
        assertThrows(IllegalArgumentException.class, () -> new Appointment("A1", futureDate(2), "X".repeat(51)));
    }

    @Test
    void settersUpdateFieldsCorrectly() {
        Appointment a = new Appointment("A1", futureDate(5), "Original");
        a.setDescription("Updated");
        assertEquals("Updated", a.getDescription());

        Date newDate = futureDate(10);
        a.setAppointmentDate(newDate);
        assertEquals(newDate, a.getAppointmentDate());
    }
    
    @Test
    void setAppointmentDate_invalidCases_throwException() {
        Appointment a = new Appointment("A1", futureDate(2), "Desc");
        // null date
        assertThrows(IllegalArgumentException.class, () -> a.setAppointmentDate(null));
        // past date
        Date past = new Date(System.currentTimeMillis() - 86400000);
        assertThrows(IllegalArgumentException.class, () -> a.setAppointmentDate(past));
    }

    @Test
    void setDescription_invalidCases_throwException() {
        Appointment a = new Appointment("A1", futureDate(2), "Desc");
        assertThrows(IllegalArgumentException.class, () -> a.setDescription(null));
        assertThrows(IllegalArgumentException.class, () -> a.setDescription("X".repeat(51)));
    }

}

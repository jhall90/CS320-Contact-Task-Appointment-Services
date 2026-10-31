package projectone;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages adding, updating, and deleting appointments.
 */
public class AppointmentService {

    // In-memory storage for appointments
    private final Map<String, Appointment> appointments = new HashMap<>();

    /**
     * Add a new appointment.
     * Throws exception if ID already exists.
     */
    public void addAppointment(Appointment appointment) {
        if (appointments.containsKey(appointment.getAppointmentId())) {
            throw new IllegalArgumentException("Appointment ID already exists");
        }
        appointments.put(appointment.getAppointmentId(), appointment);
    }

    /**
     * Delete an appointment by ID.
     * Returns true if removed, false if not found.
     */
    public boolean deleteAppointment(String appointmentId) {
        return appointments.remove(appointmentId) != null;
    }

    /**
     * Update appointment date.
     */
    public void updateAppointmentDate(String appointmentId, java.util.Date newDate) {
        Appointment appt = appointments.get(appointmentId);
        if (appt == null) {
            throw new IllegalArgumentException("Appointment not found");
        }
        appt.setAppointmentDate(newDate);
    }

    /**
     * Update appointment description.
     */
    public void updateAppointmentDescription(String appointmentId, String newDescription) {
        Appointment appt = appointments.get(appointmentId);
        if (appt == null) {
            throw new IllegalArgumentException("Appointment not found");
        }
        appt.setDescription(newDescription);
    }

    // Return all appointments
    public Map<String, Appointment> getAllAppointments() {
        return appointments;
    }
}

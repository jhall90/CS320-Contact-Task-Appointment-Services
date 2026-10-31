package projectone;

import java.util.Date;

/**
 * Represents an Appointment with ID, date, and description.
 */
public class Appointment {

    // Unique appointment ID, cannot be updated
    private final String appointmentId;

    // Appointment date, cannot be in the past
    private Date appointmentDate;

    // Appointment description
    private String description;

    /**
     * Constructor that validates and initializes fields.
     */
    public Appointment(String appointmentId, Date appointmentDate, String description) {
        if (appointmentId == null || appointmentId.length() > 10) {
            throw new IllegalArgumentException("Invalid appointment ID");
        }
        if (appointmentDate == null || appointmentDate.before(new Date())) {
            throw new IllegalArgumentException("Invalid appointment date");
        }
        if (description == null || description.length() > 50) {
            throw new IllegalArgumentException("Invalid appointment description");
        }

        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.description = description;
    }

    // Getters
    public String getAppointmentId() {
        return appointmentId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Set a new date for the appointment.
     * Date must not be in the past.
     */
    public void setAppointmentDate(Date appointmentDate) {
        if (appointmentDate == null || appointmentDate.before(new Date())) {
            throw new IllegalArgumentException("Invalid appointment date");
        }
        this.appointmentDate = appointmentDate;
    }

    /**
     * Set a new description for the appointment.
     */
    public void setDescription(String description) {
        if (description == null || description.length() > 50) {
            throw new IllegalArgumentException("Invalid appointment description");
        }
        this.description = description;
    }
}

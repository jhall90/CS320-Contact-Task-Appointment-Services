package projectone.enhanced.appointment;

import java.time.LocalDateTime;

import projectone.enhanced.common.OwnedEntity;

/*
 * Uses java.time.LocalDateTime instead of java.util.Date
 * LocalDateTime is immutable
 */
public class Appointment extends OwnedEntity {

    private LocalDateTime appointmentDate;
    private String description;

    public Appointment(String id, String owner, LocalDateTime appointmentDate, String description) {
        super(id, owner);
        this.appointmentDate = appointmentDate;
        this.description = description;
    }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public String getDescription() { return description; }

    void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }
    void setDescription(String description) { this.description = description; }
}

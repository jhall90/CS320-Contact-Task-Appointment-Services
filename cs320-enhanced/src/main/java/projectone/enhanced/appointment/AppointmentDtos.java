package projectone.enhanced.appointment;

import static projectone.enhanced.common.ValidationRules.DESCRIPTION_MAX;
import static projectone.enhanced.common.ValidationRules.ID_MAX;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class AppointmentDtos {

    private AppointmentDtos() {
    }

    /* Dates are in "2027-01-15T14:30:00" format. */
    public record CreateAppointmentRequest(
            @NotBlank @Size(max = ID_MAX) String appointmentId,
            @NotNull @FutureOrPresent(message = "cannot be in the past") LocalDateTime appointmentDate,
            @NotBlank @Size(max = DESCRIPTION_MAX) String description) {
    }

    public record UpdateAppointmentRequest(
            @FutureOrPresent(message = "cannot be in the past") LocalDateTime appointmentDate,
            @Size(min = 1, max = DESCRIPTION_MAX) String description) {
    }

    public record AppointmentResponse(String appointmentId, LocalDateTime appointmentDate,
                                      String description) {

        static AppointmentResponse from(Appointment a) {
            return new AppointmentResponse(a.getId(), a.getAppointmentDate(), a.getDescription());
        }
    }
}

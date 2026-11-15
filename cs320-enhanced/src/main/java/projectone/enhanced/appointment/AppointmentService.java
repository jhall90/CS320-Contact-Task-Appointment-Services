package projectone.enhanced.appointment;

import org.springframework.stereotype.Service;

import projectone.enhanced.appointment.AppointmentDtos.CreateAppointmentRequest;
import projectone.enhanced.appointment.AppointmentDtos.UpdateAppointmentRequest;
import projectone.enhanced.common.AbstractOwnedService;
import projectone.enhanced.common.CurrentUser;

@Service
public class AppointmentService extends AbstractOwnedService<Appointment> {

    public AppointmentService(AppointmentRepository repository) {
        super(repository, "Appointment");
    }

    public Appointment create(CreateAppointmentRequest req, CurrentUser user) {
        return insert(new Appointment(req.appointmentId(), user.username(),
                req.appointmentDate(), req.description()));
    }

    public Appointment update(String id, UpdateAppointmentRequest req, CurrentUser user) {
        Appointment appt = get(id, user);
        synchronized (appt) {
            if (req.appointmentDate() != null) appt.setAppointmentDate(req.appointmentDate());
            if (req.description() != null) appt.setDescription(req.description());
        }
        return appt;
    }
}

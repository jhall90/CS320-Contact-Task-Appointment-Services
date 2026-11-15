package projectone.enhanced.appointment;

import static projectone.enhanced.common.ValidationRules.ID_MAX;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import projectone.enhanced.appointment.AppointmentDtos.AppointmentResponse;
import projectone.enhanced.appointment.AppointmentDtos.CreateAppointmentRequest;
import projectone.enhanced.appointment.AppointmentDtos.UpdateAppointmentRequest;
import projectone.enhanced.common.CurrentUser;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<AppointmentResponse> list(Authentication auth) {
        return service.list(CurrentUser.from(auth)).stream().map(AppointmentResponse::from).toList();
    }

    @GetMapping("/{id}")
    public AppointmentResponse get(@PathVariable @Size(max = ID_MAX) String id, Authentication auth) {
        return AppointmentResponse.from(service.get(id, CurrentUser.from(auth)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse create(@Valid @RequestBody CreateAppointmentRequest req, Authentication auth) {
        return AppointmentResponse.from(service.create(req, CurrentUser.from(auth)));
    }

    @PatchMapping("/{id}")
    public AppointmentResponse update(@PathVariable @Size(max = ID_MAX) String id,
                                      @Valid @RequestBody UpdateAppointmentRequest req,
                                      Authentication auth) {
        return AppointmentResponse.from(service.update(id, req, CurrentUser.from(auth)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Size(max = ID_MAX) String id, Authentication auth) {
        service.delete(id, CurrentUser.from(auth));
    }
}

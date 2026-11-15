package projectone.enhanced.task;

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
import projectone.enhanced.common.CurrentUser;
import projectone.enhanced.task.TaskDtos.CreateTaskRequest;
import projectone.enhanced.task.TaskDtos.TaskResponse;
import projectone.enhanced.task.TaskDtos.UpdateTaskRequest;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaskResponse> list(Authentication auth) {
        return service.list(CurrentUser.from(auth)).stream().map(TaskResponse::from).toList();
    }

    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable @Size(max = ID_MAX) String id, Authentication auth) {
        return TaskResponse.from(service.get(id, CurrentUser.from(auth)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@Valid @RequestBody CreateTaskRequest req, Authentication auth) {
        return TaskResponse.from(service.create(req, CurrentUser.from(auth)));
    }

    @PatchMapping("/{id}")
    public TaskResponse update(@PathVariable @Size(max = ID_MAX) String id,
                               @Valid @RequestBody UpdateTaskRequest req, Authentication auth) {
        return TaskResponse.from(service.update(id, req, CurrentUser.from(auth)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Size(max = ID_MAX) String id, Authentication auth) {
        service.delete(id, CurrentUser.from(auth));
    }
}

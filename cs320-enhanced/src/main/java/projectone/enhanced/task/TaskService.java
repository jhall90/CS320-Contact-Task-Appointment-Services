package projectone.enhanced.task;

import org.springframework.stereotype.Service;

import projectone.enhanced.common.AbstractOwnedService;
import projectone.enhanced.common.CurrentUser;
import projectone.enhanced.task.TaskDtos.CreateTaskRequest;
import projectone.enhanced.task.TaskDtos.UpdateTaskRequest;

@Service
public class TaskService extends AbstractOwnedService<Task> {

    public TaskService(TaskRepository repository) {
        super(repository, "Task");
    }

    public Task create(CreateTaskRequest req, CurrentUser user) {
        return insert(new Task(req.taskId(), user.username(), req.name(), req.description()));
    }

    public Task update(String id, UpdateTaskRequest req, CurrentUser user) {
        Task task = get(id, user);
        synchronized (task) {
            if (req.name() != null) task.setName(req.name());
            if (req.description() != null) task.setDescription(req.description());
        }
        return task;
    }
}

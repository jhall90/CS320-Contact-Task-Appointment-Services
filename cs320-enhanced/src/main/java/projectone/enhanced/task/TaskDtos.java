package projectone.enhanced.task;

import static projectone.enhanced.common.ValidationRules.DESCRIPTION_MAX;
import static projectone.enhanced.common.ValidationRules.ID_MAX;
import static projectone.enhanced.common.ValidationRules.TASK_NAME_MAX;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class TaskDtos {

    private TaskDtos() {
    }

    public record CreateTaskRequest(
            @NotBlank @Size(max = ID_MAX) String taskId,
            @NotBlank @Size(max = TASK_NAME_MAX) String name,
            @NotBlank @Size(max = DESCRIPTION_MAX) String description) {
    }

    public record UpdateTaskRequest(
            @Size(min = 1, max = TASK_NAME_MAX) String name,
            @Size(min = 1, max = DESCRIPTION_MAX) String description) {
    }

    public record TaskResponse(String taskId, String name, String description) {

        static TaskResponse from(Task t) {
            return new TaskResponse(t.getId(), t.getName(), t.getDescription());
        }
    }
}

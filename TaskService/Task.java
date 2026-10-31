package projectone;

/**
 * Represents a Task with ID, name, and description.
 */
public class Task {

    // Create variables
    private final String taskId;
    private String name;
    private String description;

    /**
     * Constructor for creating a new Task object.
     * Validates ID, name, and description.
     */
    public Task(String taskId, String name, String description) {
        if (taskId == null || taskId.length() > 10) {
            throw new IllegalArgumentException("Invalid task ID");
        }
        if (name == null || name.length() > 20) {
            throw new IllegalArgumentException("Invalid task name");
        }
        if (description == null || description.length() > 50) {
            throw new IllegalArgumentException("Invalid task description");
        }
        this.taskId = taskId;
        this.name = name;
        this.description = description;
    }

    // Getter for task ID
    public String getTaskId() {
        return taskId;
    }

    // Getter for task name
    public String getName() {
        return name;
    }

    // Getter for task description
    public String getDescription() {
        return description;
    }

    // Update name of the task
    public void setName(String name) {
        if (name == null || name.length() > 20) {
            throw new IllegalArgumentException("Invalid task name");
        }
        this.name = name;
    }

    // Update description of the task
    public void setDescription(String description) {
        if (description == null || description.length() > 50) {
            throw new IllegalArgumentException("Invalid task description");
        }
        this.description = description;
    }
}

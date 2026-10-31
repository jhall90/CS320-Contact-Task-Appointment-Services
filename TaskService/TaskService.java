package projectone;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides services to add, update, and delete tasks
 * Stores tasks in memory with a HashMap
 */
public class TaskService {

    // In-memory storage for tasks, keyed by taskId
    private final Map<String, Task> tasks = new HashMap<>();

    /**
     * Add a task to the service
     * taskId is unique
     */
    public void addTask(Task task) {
        if (tasks.containsKey(task.getTaskId())) {
            throw new IllegalArgumentException("Task ID already exists");
        }
        tasks.put(task.getTaskId(), task);
    }

    /**
     * Delete a task by ID
     * Returns true if task was removed, false if not found
     */
    public boolean deleteTask(String taskId) {
        return tasks.remove(taskId) != null;
    }

    // Update the name of a task by ID
    public void updateTaskName(String taskId, String name) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found");
        }
        task.setName(name);
    }

    // Update the description of a task by ID
    public void updateTaskDescription(String taskId, String description) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found");
        }
        task.setDescription(description);
    }

    // Getter for all tasks
    public Map<String, Task> getAllTasks() {
        return tasks;
    }
}

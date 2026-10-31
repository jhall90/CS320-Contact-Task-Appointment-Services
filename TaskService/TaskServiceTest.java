package projectone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TaskService class.
 */
public class TaskServiceTest {

    private TaskService service;

    // Create a new TaskService before each test
    @BeforeEach
    void setUp() {
        service = new TaskService();
    }

    // Test adding tasks
    @Test
    void addTaskAddsUniqueTasks() {
        Task t1 = new Task("1", "Task1", "Description1");
        service.addTask(t1);
        assertEquals(1, service.getAllTasks().size());

        Task t2 = new Task("2", "Task2", "Description2");
        service.addTask(t2);
        assertEquals(2, service.getAllTasks().size());

        // Adding duplicate ID should throw
        Task duplicate = new Task("1", "OtherTask", "OtherDescription");
        assertThrows(IllegalArgumentException.class, () -> service.addTask(duplicate));
    }

    // Test deleting tasks
    @Test
    void deleteTaskRemovesById() {
        Task t = new Task("1", "Task", "Description");
        service.addTask(t);

        assertTrue(service.deleteTask("1"));
        assertFalse(service.deleteTask("1")); // already removed
    }

    // Test updating name and description
    @Test
    void updateTaskFields() {
        Task t = new Task("1", "OldName", "OldDescription");
        service.addTask(t);

        service.updateTaskName("1", "NewName");
        service.updateTaskDescription("1", "NewDescription");

        Task updated = service.getAllTasks().get("1");
        assertEquals("NewName", updated.getName());
        assertEquals("NewDescription", updated.getDescription());
    }

    // Test updating non-existent task throws exception
    @Test
    void updateNonexistentTaskThrows() {
        assertThrows(IllegalArgumentException.class, () -> service.updateTaskName("missing", "Name"));
    }
    
    @Test
    void updateTaskDescription_invalidInput_throws() {
        Task t = new Task("1", "Name", "Desc");
        service.addTask(t);
        assertThrows(IllegalArgumentException.class, () -> service.updateTaskDescription("1", null));
    }

}

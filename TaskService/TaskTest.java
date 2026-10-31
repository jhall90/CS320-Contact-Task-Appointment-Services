package projectone;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Task class.
 */
public class TaskTest {

    // Test constructor creates valid Task
    @Test
    void constructorCreatesValidTask() {
        Task task = new Task("1", "TestName", "TestDescription");
        assertEquals("1", task.getTaskId());
        assertEquals("TestName", task.getName());
        assertEquals("TestDescription", task.getDescription());
    }

    // Test invalid task ID throws exception
    @Test
    void invalidTaskIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Task(null, "Name", "Description"));
        assertThrows(IllegalArgumentException.class, () -> new Task("12345678901", "Name", "Description"));
    }

    // Test invalid name throws exception
    @Test
    void invalidNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Task("1", null, "Description"));
        assertThrows(IllegalArgumentException.class, () -> new Task("1", "ThisNameIsWayTooLongForThis", "Description"));
    }

    // Test invalid description throws exception
    @Test
    void invalidDescriptionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Task("1", "Name", null));
        assertThrows(IllegalArgumentException.class, () -> new Task("1", "Name", "This description is definitely more than fifty characters long"));
    }

    // Test setters update fields correctly
    @Test
    void settersUpdateFields() {
        Task task = new Task("1", "OldName", "OldDescription");
        task.setName("NewName");
        task.setDescription("NewDescription");
        assertEquals("NewName", task.getName());
        assertEquals("NewDescription", task.getDescription());
    }
    
    @Test
    void setName_null_throws() {
        Task task = new Task("1", "ValidName", "Valid description");
        assertThrows(IllegalArgumentException.class, () -> task.setName(null));
    }

    @Test
    void setDescription_null_throws() {
        Task task = new Task("1", "ValidName", "Valid description");
        assertThrows(IllegalArgumentException.class, () -> task.setDescription(null));
    }

    @Test
    void setName_overLength_throws() {
        Task task = new Task("1", "ValidName", "Valid description");
        assertThrows(IllegalArgumentException.class, () -> task.setName("X".repeat(21)));
    }

    @Test
    void setDescription_overLength_throws() {
        Task task = new Task("1", "ValidName", "Valid description");
        assertThrows(IllegalArgumentException.class, () -> task.setDescription("Y".repeat(51)));
    }
}

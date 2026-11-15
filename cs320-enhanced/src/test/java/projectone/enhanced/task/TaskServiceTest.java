package projectone.enhanced.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import projectone.enhanced.common.CurrentUser;
import projectone.enhanced.common.DuplicateResourceException;
import projectone.enhanced.common.ResourceNotFoundException;
import projectone.enhanced.task.Task;
import projectone.enhanced.task.TaskRepository;
import projectone.enhanced.task.TaskService;
import projectone.enhanced.task.TaskDtos.CreateTaskRequest;
import projectone.enhanced.task.TaskDtos.UpdateTaskRequest;

class TaskServiceTest {

    private static final CurrentUser ALICE = new CurrentUser("alice", false);
    private static final CurrentUser BOB = new CurrentUser("bob", false);
    private static final CurrentUser ADMIN = new CurrentUser("admin", true);

    private TaskService service;

    @BeforeEach
    void setUp() {
        service = new TaskService(new TaskRepository());
        service.create(new CreateTaskRequest("T1", "Write report", "Milestone two"), ALICE);
    }

    @Test
    void createAndGet() {
        Task t = service.get("T1", ALICE);
        assertEquals("Write report", t.getName());
        assertEquals("alice", t.getOwner());
    }

    @Test
    void duplicateRejected() {
        assertThrows(DuplicateResourceException.class,
                () -> service.create(new CreateTaskRequest("T1", "N", "D"), ALICE));
    }

    @Test
    void partialUpdate() {
        service.update("T1", new UpdateTaskRequest(null, "New description"), ALICE);
        Task t = service.update("T1", new UpdateTaskRequest("New name", null), ALICE);
        assertEquals("New name", t.getName());
        assertEquals("New description", t.getDescription());
    }

    @Test
    void ownershipEnforced() {
        assertThrows(ResourceNotFoundException.class, () -> service.get("T1", BOB));
        assertThrows(ResourceNotFoundException.class, () -> service.delete("T1", BOB));
        assertEquals("Write report", service.get("T1", ADMIN).getName());
    }

    @Test
    void delete() {
        service.delete("T1", ALICE);
        assertEquals(0, service.list(ALICE).size());
    }
}

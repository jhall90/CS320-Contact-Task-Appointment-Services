package projectone.enhanced.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;

import projectone.enhanced.common.DuplicateResourceException;
import projectone.enhanced.common.InMemoryRepository;
import projectone.enhanced.task.Task;

class InMemoryRepositoryTest {

    private final InMemoryRepository<Task> repo = new InMemoryRepository<>();

    @Test
    void insertAndFind() {
        repo.insert(new Task("1", "alice", "Name", "Desc"));
        assertTrue(repo.findById("1").isPresent());
        assertFalse(repo.findById("2").isPresent());
    }

    @Test
    void duplicateIdIsRejected() {
        repo.insert(new Task("1", "alice", "Name", "Desc"));
        assertThrows(DuplicateResourceException.class,
                () -> repo.insert(new Task("1", "bob", "Other", "Desc")));
    }

    @Test
    void findByOwnerOnlyReturnsThatOwnersRecords() {
        repo.insert(new Task("1", "alice", "A", "Desc"));
        repo.insert(new Task("2", "bob", "B", "Desc"));
        repo.insert(new Task("3", "alice", "C", "Desc"));
        assertEquals(2, repo.findByOwner("alice").size());
        assertEquals(3, repo.findAll().size());
    }

    @Test
    void deleteReturnsWhetherSomethingWasRemoved() {
        repo.insert(new Task("1", "alice", "A", "Desc"));
        assertTrue(repo.deleteById("1"));
        assertFalse(repo.deleteById("1"));
    }

    @Test
    void concurrentInsertsWithSameIdOnlyOneWins() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        List<Callable<Boolean>> jobs = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            final String owner = "user" + i;
            jobs.add(() -> {
                try {
                    repo.insert(new Task("same", owner, "A", "Desc"));
                    return true;
                } catch (DuplicateResourceException e) {
                    return false;
                }
            });
        }
        int successes = 0;
        for (Future<Boolean> f : pool.invokeAll(jobs)) {
            if (f.get()) successes++;
        }
        pool.shutdown();
        assertEquals(1, successes);
    }

    @Test
    void entityRequiresIdAndOwner() {
        assertThrows(IllegalArgumentException.class, () -> new Task(null, "alice", "A", "D"));
        assertThrows(IllegalArgumentException.class, () -> new Task("1", " ", "A", "D"));
    }
}

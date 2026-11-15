package projectone.enhanced.task;

import org.springframework.stereotype.Repository;

import projectone.enhanced.common.InMemoryRepository;

@Repository
public class TaskRepository extends InMemoryRepository<Task> {
}

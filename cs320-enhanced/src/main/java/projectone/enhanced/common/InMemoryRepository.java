package projectone.enhanced.common;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Generic in-memory store
 * Replaces the three copies of HashMap logic in the original ContactService, TaskService, and AppointmentService.
 * ConcurrentHashMap keeps it safe when the web server handles requests on several threads at once.
 */
public class InMemoryRepository<T extends OwnedEntity> {

    private final ConcurrentMap<String, T> store = new ConcurrentHashMap<>();

    /*
     * Adds a new entity. putIfAbsent makes the duplicate check and the insert a single atomic step.
     * Two requests with the same id cannot both work.
     */
    public T insert(T entity) {
        T existing = store.putIfAbsent(entity.getId(), entity);
        if (existing != null) {
            throw new DuplicateResourceException(entity.getId());
        }
        return entity;
    }

    public Optional<T> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<T> findAll() {
        return List.copyOf(store.values());
    }

    public List<T> findByOwner(String owner) {
        return store.values().stream()
                .filter(e -> e.getOwner().equals(owner))
                .toList();
    }

    public boolean deleteById(String id) {
        return store.remove(id) != null;
    }

    public void clear() {
        store.clear();
    }
}

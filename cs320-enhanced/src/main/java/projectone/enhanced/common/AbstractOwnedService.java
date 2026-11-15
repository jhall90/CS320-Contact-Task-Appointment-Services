package projectone.enhanced.common;

import java.util.List;

/*
 * Shared service logic
 */
public abstract class AbstractOwnedService<T extends OwnedEntity> {

    private final InMemoryRepository<T> repository;
    private final String resourceName;

    protected AbstractOwnedService(InMemoryRepository<T> repository, String resourceName) {
        this.repository = repository;
        this.resourceName = resourceName;
    }

    public List<T> list(CurrentUser user) {
        return user.admin() ? repository.findAll() : repository.findByOwner(user.username());
    }

    /*
     * Looks up a record and checks ownership. 
     * A record owned by someone else is reported as "not found" so callers cannot brute force for other users' ids.
     */
    public T get(String id, CurrentUser user) {
        return repository.findById(id)
                .filter(user::canAccess)
                .orElseThrow(() -> new ResourceNotFoundException(resourceName, id));
    }

    public void delete(String id, CurrentUser user) {
        T entity = get(id, user);
        repository.deleteById(entity.getId());
    }

    protected T insert(T entity) {
        return repository.insert(entity);
    }
}

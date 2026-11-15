package projectone.enhanced.common;

/*
 * Base class for every record a user can own (contacts, tasks, appointments).
 * The id is immutable
 */
public abstract class OwnedEntity {

    private final String id;
    private final String owner;

    protected OwnedEntity(String id, String owner) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("owner is required");
        }
        this.id = id;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }
}

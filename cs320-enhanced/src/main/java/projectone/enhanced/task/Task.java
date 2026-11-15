package projectone.enhanced.task;

import projectone.enhanced.common.OwnedEntity;

public class Task extends OwnedEntity {

    private String name;
    private String description;

    public Task(String id, String owner, String name, String description) {
        super(id, owner);
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    void setName(String name) { this.name = name; }
    void setDescription(String description) { this.description = description; }
}

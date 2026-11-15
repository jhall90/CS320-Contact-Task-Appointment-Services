package projectone.enhanced.common;

public class DuplicateResourceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

    public DuplicateResourceException(String id) {
        super("A record with id '" + id + "' already exists");
    }
}

package projectone.enhanced.common;

public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String resourceName, String id) {
        super(resourceName + " not found: " + id);
    }
}

package projectone;

import org.junit.jupiter.api.Test;

import projectone.Contact;

import static org.junit.jupiter.api.Assertions.*;

public class ContactTest {
	
	@Test
	public void constructor_validCreatesContact() {
		Contact c = new Contact("12345", "Josh", "Hall", "1234567890", "1 Main St, Town");
		assertEquals("12345", c.getContactId());
		assertEquals("Josh", c.getFirstName());
		assertEquals("Hall", c.getLastName());
		assertEquals("1234567890", c.getPhone());
		assertEquals("1 Main St, Town", c.getAddress());
	}
	
	
	@Test
	public void constructor_invalidId() {
		assertThrows(IllegalArgumentException.class, () -> new Contact(null, "A", "B", "1234567890", "1 Main St, Town"));
		assertThrows(IllegalArgumentException.class, () -> new Contact("01234567890", "A", "B", "1234567890", "1 Main St, Town"));
	}
	
	
	@Test
	public void constructor_invalidFirstName() {
		assertThrows(IllegalArgumentException.class, () -> new Contact("1", null, "B", "1234567890", "1 Main St, Town"));
		assertThrows(IllegalArgumentException.class, () -> new Contact("1", "01234567890", "B", "1234567890", "1 Main St, Town"));
	}
	
	
	@Test
	public void constructor_invalidLastName() {
		assertThrows(IllegalArgumentException.class, () -> new Contact("1", "A", null, "1234567890", "1 Main St, Town"));
		assertThrows(IllegalArgumentException.class, () -> new Contact("1", "A", "01234567890", "1234567890", "1 Main St, Town"));
	}
	
	
	@Test
	public void constructor_invalidPhone() {
		assertThrows(IllegalArgumentException.class, () -> new Contact("1", "A", "B", null, "1 Main St, Town"));
		assertThrows(IllegalArgumentException.class, () -> new Contact("1", "A", "B", "123456789", "1 Main St, Town"));
		assertThrows(IllegalArgumentException.class, () -> new Contact("1", "A", "B", "12345678901", "1 Main St, Town"));
		assertThrows(IllegalArgumentException.class, () -> new Contact("1", "A", "B", "12345abcde", "1 Main St, Town"));
	}
	
	
	@Test
	public void constructor_invalidAddress() {
		assertThrows(IllegalArgumentException.class, () -> new Contact("1", "A", "B", "1234567890", null));
		assertThrows(IllegalArgumentException.class, () -> new Contact("1", "A", "B", "1234567890", "x".repeat(31)));
	}
	
	
	@Test
	public void setters_enforceValidation() {
		Contact c = new Contact("1", "A", "B", "1234567890", "addr");
		assertThrows(IllegalArgumentException.class, () -> c.setFirstName(null));
		assertThrows(IllegalArgumentException.class, () -> c.setLastName("01234567890"));
		assertThrows(IllegalArgumentException.class, () -> c.setPhone("abcdef1234"));
		assertThrows(IllegalArgumentException.class, () -> c.setAddress("x".repeat(31)));
	}
	
	
	@Test
	public void contactId_isNotUpdatable() {
		Contact c = new Contact("abc", "A", "B", "1234567890", "1 Main St, Town");
			// Ensure no setter exists for contactId by checking only getters compile;
			assertEquals("abc", c.getContactId());
		}
}
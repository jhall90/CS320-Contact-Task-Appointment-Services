package projectone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import projectone.Contact;
import projectone.ContactService;

import static org.junit.jupiter.api.Assertions.*;

public class ContactServiceTest {
	
	private ContactService service;
	
	
	@BeforeEach
		void setUp() {
		service = new ContactService();
	}
	
	
	@Test
	public void addContact_noDuplicates() {
		Contact c1 = new Contact("1", "A", "B", "1111111111", "addr1");
		service.addContact(c1);
		assertEquals(1, service.getAllContacts().size());
		
		
		Contact c2 = new Contact("2", "C", "D", "2222222222", "addr2");
		service.addContact(c2);
		assertEquals(2, service.getAllContacts().size());
		
		
		Contact dup = new Contact("1", "E", "F", "3333333333", "addr3");
		assertThrows(IllegalArgumentException.class, () -> service.addContact(dup));
		assertEquals(2, service.getAllContacts().size());
	}
	
	
	@Test
	public void deleteContact_returnsFlag() {
		Contact c = new Contact("1", "A", "B", "1111111111", "addr1");
		service.addContact(c);
		assertTrue(service.deleteContact("1"));
		assertFalse(service.deleteContact("1")); // already removed
		assertEquals(0, service.getAllContacts().size());
	}
	
	
	@Test
	public void updateFields_byId() {
		Contact c = new Contact("9", "Josh", "Hall", "9999999999", "123 Ave");
		service.addContact(c);
		
	
		service.updateFirstName("9", "Josh");
		service.updateLastName("9", "Hall");
		service.updatePhone("9", "0123456789");
		service.updateAddress("9", "456 Road, City");
		
	
		Contact updated = service.getAllContacts().get("9");
		assertEquals("Josh", updated.getFirstName());
		assertEquals("Hall", updated.getLastName());
		assertEquals("0123456789", updated.getPhone());
		assertEquals("456 Road, City", updated.getAddress());
	
	
		// invalid updates throw
		assertThrows(IllegalArgumentException.class, () -> service.updatePhone("9", "bad-number"));
	}
	
	
	@Test
	public void update_or_delete_nonexistentId() {
		assertFalse(service.deleteContact("missing"));
		assertThrows(IllegalArgumentException.class, () -> service.updateFirstName("missing", "X"));
	}
	
	@Test
	void nullArguments_throwException() {
	    assertThrows(IllegalArgumentException.class, () -> service.addContact(null));
	    assertThrows(IllegalArgumentException.class, () -> service.deleteContact(null));
	    assertThrows(IllegalArgumentException.class, () -> service.updateFirstName(null, "Name"));
	}

}
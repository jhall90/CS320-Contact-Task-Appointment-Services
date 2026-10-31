package projectone;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
 * In-memory ContactService
 * Use contactId to add, delete, update
 * */
public class ContactService {
	private final Map<String, Contact> contacts = new HashMap<>(); 
	
	/* 
	 * Add contact
	 * contactId must be unique per the requirements
	 */
	public void addContact(Contact contact) {
		if (contact == null) {
			throw new IllegalArgumentException("Contact must not be null");
		}
		String id = contact.getContactId();
		if (contacts.containsKey(id)) {
			throw new IllegalArgumentException("Duplicate contactId: " + id);
		}
		contacts.put(id, contact);
	}
	
	/*
	 * Delete contact by Id
	 * return True if it removes successfully
	 * else return False
	 * */
	public boolean deleteContact(String contactId) {
		if (contactId == null) {
		throw new IllegalArgumentException("contactId cannot be null");
		}
		return contacts.remove(contactId) != null;
	}

	// Update helpers
	public void updateFirstName(String contactId, String firstName) {
		Contact c = getRequired(contactId);
		c.setFirstName(firstName);
	}

	public void updateLastName(String contactId, String lastName) {
		Contact c = getRequired(contactId);
		c.setLastName(lastName);
	}

	public void updatePhone(String contactId, String phone) {
		Contact c = getRequired(contactId);
		c.setPhone(phone);
	}

	public void updateAddress(String contactId, String address) {
		Contact c = getRequired(contactId);
		c.setAddress(address);
	}	
	
	/*
	 * Get a Map view of contacts 
	 * this is not modifiable and just a view of the Map that was made
	 */
	public Map<String, Contact> getAllContacts() {
		return Collections.unmodifiableMap(contacts);
	}
	
	// Check that the contactId is not null and exists
	private Contact getRequired(String contactId) {
		if (contactId == null) {
			throw new IllegalArgumentException("contactId cannot be null");
		}
		Contact c = contacts.get(contactId);
		if (c == null) {
			throw new IllegalArgumentException("No contact with id: " + contactId);
		}
		return c;
	}
}

package projectone;

import java.util.Objects;

public class Contact {
	// Declare
	private final String contactId; // THIS IS IMMUTABLE
	private String firstName;
	private String lastName;
	private String phone;
	private String address;
	
	// Constructor
	public Contact(String contactId, String firstName, String lastName, String phone, String address) {
		this.contactId = validateId(contactId);
		this.firstName = validateName(firstName, "First Name");
		this.lastName = validateName(lastName, "Last Name");
		this.phone = validatePhone(phone);
		this.address = validateAddress(address);
	}
	
	// Validation Methods
	private static String validateId(String id) {
		if (id == null || id.length() == 0 || id.length() > 10) {
			throw new IllegalArgumentException("Contact ID must be 1-10 chars and not null");
		}
		return id;
	}


	private static String validateName(String name, String field) {
		if (name == null || name.length() == 0 || name.length() > 10) {
			throw new IllegalArgumentException(field + " must be 1-10 chars and not null");
		}
		return name;
	}


	private static String validatePhone(String phone) {
		if (phone == null || !phone.matches("\\d{10}")) {
			throw new IllegalArgumentException("phone must be exactly 10 digits");
		}
		return phone;
	}


	private static String validateAddress(String address) {
		if (address == null || address.length() == 0 || address.length() > 30) {
			throw new IllegalArgumentException("address must be 1-30 chars and not null");
		}
		return address;
	}
	
	// Getters
	public String getContactId() { return contactId; }
	public String getFirstName() { return firstName; }
	public String getLastName() { return lastName; }
	public String getPhone() { return phone; }
	public String getAddress() { return address; }


	// Setters (no setter for contactId)
	public void setFirstName(String firstName) { this.firstName = validateName(firstName, "First Name"); }
	public void setLastName(String lastName) { this.lastName = validateName(lastName, "Last Name"); }
	public void setPhone(String phone) { this.phone = validatePhone(phone); }
	public void setAddress(String address) { this.address = validateAddress(address); }
	
	// Since we are using in-memory data structures I am overriding two objects methods
	
	/*
	 * If two contacts have the same contactId means they are the same object
	 * 
	 * ex: Contact("123", "Josh", "Hall") would equal Contact("123", "John", "Smith")
	 * */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Contact)) return false;
		Contact contact = (Contact) o;
		return contactId.equals(contact.contactId);
	}

	/*
	 * Make sure if two contacts are equal by the equals method that they are hashed the same
	 * 
	 * */
	@Override
	public int hashCode() {
		return Objects.hash(contactId);
	}
}

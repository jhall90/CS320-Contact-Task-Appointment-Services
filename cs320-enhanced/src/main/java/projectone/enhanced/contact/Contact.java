package projectone.enhanced.contact;

import projectone.enhanced.common.OwnedEntity;

/*
 * Contact record
 * Input rules are enforced on the request DTOs
 * Only holds the state
 * The id and owner cannot change after creation
 */
public class Contact extends OwnedEntity {

    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    public Contact(String id, String owner, String firstName, String lastName,
                   String phone, String address) {
        super(id, owner);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    void setFirstName(String firstName) { this.firstName = firstName; }
    void setLastName(String lastName) { this.lastName = lastName; }
    void setPhone(String phone) { this.phone = phone; }
    void setAddress(String address) { this.address = address; }
}

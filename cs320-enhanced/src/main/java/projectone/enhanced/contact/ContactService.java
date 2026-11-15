package projectone.enhanced.contact;

import org.springframework.stereotype.Service;

import projectone.enhanced.common.AbstractOwnedService;
import projectone.enhanced.common.CurrentUser;
import projectone.enhanced.contact.ContactDtos.CreateContactRequest;
import projectone.enhanced.contact.ContactDtos.UpdateContactRequest;

@Service
public class ContactService extends AbstractOwnedService<Contact> {

    public ContactService(ContactRepository repository) {
        super(repository, "Contact");
    }

    public Contact create(CreateContactRequest req, CurrentUser user) {
        Contact contact = new Contact(req.contactId(), user.username(), req.firstName(),
                req.lastName(), req.phone(), req.address());
        return insert(contact);
    }

    /* One update method instead of the 4 from the original */
    public Contact update(String id, UpdateContactRequest req, CurrentUser user) {
        Contact contact = get(id, user);
        synchronized (contact) {
            if (req.firstName() != null) contact.setFirstName(req.firstName());
            if (req.lastName() != null) contact.setLastName(req.lastName());
            if (req.phone() != null) contact.setPhone(req.phone());
            if (req.address() != null) contact.setAddress(req.address());
        }
        return contact;
    }
}

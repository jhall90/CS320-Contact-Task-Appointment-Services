package projectone.enhanced.contact;

import static projectone.enhanced.common.ValidationRules.ADDRESS_MAX;
import static projectone.enhanced.common.ValidationRules.CONTACT_NAME_MAX;
import static projectone.enhanced.common.ValidationRules.ID_MAX;
import static projectone.enhanced.common.ValidationRules.PHONE_PATTERN;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/*
 * Request and response for the contact endpoints. 
 */
public final class ContactDtos {

    private ContactDtos() {
    }

    public record CreateContactRequest(
            @NotBlank @Size(max = ID_MAX) String contactId,
            @NotBlank @Size(max = CONTACT_NAME_MAX) String firstName,
            @NotBlank @Size(max = CONTACT_NAME_MAX) String lastName,
            @NotBlank @Pattern(regexp = PHONE_PATTERN, message = "must be exactly 10 digits") String phone,
            @NotBlank @Size(max = ADDRESS_MAX) String address) {
    }

    /** PATCH body. Null means "leave this field alone". */
    public record UpdateContactRequest(
            @Size(min = 1, max = CONTACT_NAME_MAX) String firstName,
            @Size(min = 1, max = CONTACT_NAME_MAX) String lastName,
            @Pattern(regexp = PHONE_PATTERN, message = "must be exactly 10 digits") String phone,
            @Size(min = 1, max = ADDRESS_MAX) String address) {
    }

    public record ContactResponse(String contactId, String firstName, String lastName,
                                  String phone, String address) {

        static ContactResponse from(Contact c) {
            return new ContactResponse(c.getId(), c.getFirstName(), c.getLastName(),
                    c.getPhone(), c.getAddress());
        }
    }
}

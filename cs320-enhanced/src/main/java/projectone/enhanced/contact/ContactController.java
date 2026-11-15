package projectone.enhanced.contact;

import static projectone.enhanced.common.ValidationRules.ID_MAX;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import projectone.enhanced.common.CurrentUser;
import projectone.enhanced.contact.ContactDtos.ContactResponse;
import projectone.enhanced.contact.ContactDtos.CreateContactRequest;
import projectone.enhanced.contact.ContactDtos.UpdateContactRequest;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @GetMapping
    public List<ContactResponse> list(Authentication auth) {
        return service.list(CurrentUser.from(auth)).stream().map(ContactResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ContactResponse get(@PathVariable @Size(max = ID_MAX) String id, Authentication auth) {
        return ContactResponse.from(service.get(id, CurrentUser.from(auth)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactResponse create(@Valid @RequestBody CreateContactRequest req, Authentication auth) {
        return ContactResponse.from(service.create(req, CurrentUser.from(auth)));
    }

    @PatchMapping("/{id}")
    public ContactResponse update(@PathVariable @Size(max = ID_MAX) String id,
                                  @Valid @RequestBody UpdateContactRequest req, Authentication auth) {
        return ContactResponse.from(service.update(id, req, CurrentUser.from(auth)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Size(max = ID_MAX) String id, Authentication auth) {
        service.delete(id, CurrentUser.from(auth));
    }
}

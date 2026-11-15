package projectone.enhanced.contact;

import org.springframework.stereotype.Repository;

import projectone.enhanced.common.InMemoryRepository;

@Repository
public class ContactRepository extends InMemoryRepository<Contact> {
}

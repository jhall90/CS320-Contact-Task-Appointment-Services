package projectone.enhanced.appointment;

import org.springframework.stereotype.Repository;

import projectone.enhanced.common.InMemoryRepository;

@Repository
public class AppointmentRepository extends InMemoryRepository<Appointment> {
}

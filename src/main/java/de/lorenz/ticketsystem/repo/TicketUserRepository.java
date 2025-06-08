package de.lorenz.ticketsystem.repo;

import de.lorenz.ticketsystem.entity.TicketUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketUserRepository extends JpaRepository<TicketUser, Long> {
    boolean existsByEmail(String email);

    boolean existsByName(String name);
}

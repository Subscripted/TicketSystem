package de.lorenz.ticketsystem.repo;

import de.lorenz.ticketsystem.entity.TicketUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketUserRepository extends JpaRepository<TicketUser, Long> {
    boolean existsByName(String name);

    Long getUserIdByName(String name);

    Optional<TicketUser> findByName(String name);
}

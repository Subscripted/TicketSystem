package de.lorenz.ticketsystem.repo;

import de.lorenz.ticketsystem.entity.Ticket;
import de.lorenz.ticketsystem.entity.TicketTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketTimeRepository extends JpaRepository<TicketTime, Long> {
    Optional<TicketTime> findByTicketId(long ticketId);
}

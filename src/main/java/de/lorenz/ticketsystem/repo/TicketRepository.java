package de.lorenz.ticketsystem.repo;

import de.lorenz.ticketsystem.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}

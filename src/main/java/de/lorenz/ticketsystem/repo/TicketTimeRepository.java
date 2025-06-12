package de.lorenz.ticketsystem.repo;

import de.lorenz.ticketsystem.entity.TicketTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketTimeRepository extends JpaRepository<TicketTime, Long> {
}

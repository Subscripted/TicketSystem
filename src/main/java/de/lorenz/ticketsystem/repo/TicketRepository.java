package de.lorenz.ticketsystem.repo;

import de.lorenz.ticketsystem.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> getTicketByType(int type);

    List<Ticket> findByType(int type);

    List<Ticket> findByAssignedUser_UserId(long assignedUserUserId);

    @Query("SELECT t FROM Ticket t WHERE t.assignedUser.id = :userId AND t.type = :type")
    List<Ticket> findByAssignedUserIdAndType(@Param("userId") Long userId, @Param("type") Integer type);}

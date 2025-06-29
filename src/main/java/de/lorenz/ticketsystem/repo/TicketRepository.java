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
    List<Ticket> findByAssignedUserIdAndType(@Param("userId") Long userId, @Param("type") Integer type);

    @Query("""
                SELECT t FROM Ticket t
                WHERE (:id IS NULL OR t.id = :id)
                AND (:assignedUserId IS NULL OR t.assignedUser.userId = :assignedUserId)
                AND (:type IS NULL OR t.type = :type)
                AND (:status IS NULL OR t.status = :status)
            """)
    List<Ticket> findByFilter(
            @Param("id") Long id,
            @Param("assignedUserId") Long assignedUserId,
            @Param("type") Integer type,
            @Param("status") Integer status
    );
}
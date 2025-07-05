package de.lorenz.ticketsystem.repo;

import de.lorenz.ticketsystem.entity.TicketLoginCreds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<TicketLoginCreds, Long> {
    Optional<TicketLoginCreds> findByEmail(String email);

    @Query("SELECT l.passwordHash FROM TicketLoginCreds l WHERE l.email = :email")
    String getPasswordHashByEmail(@Param("email") String email);
    String getPasswordHashById(Long id);
}

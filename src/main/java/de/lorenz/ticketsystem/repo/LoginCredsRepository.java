package de.lorenz.ticketsystem.repo;

import de.lorenz.ticketsystem.entity.LoginCreds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginCredsRepository extends JpaRepository<LoginCreds, Long> {

    Optional<LoginCreds> findByEmailAndClientIdAndClientSecret(String email, String clientId, String clientSecret);

}

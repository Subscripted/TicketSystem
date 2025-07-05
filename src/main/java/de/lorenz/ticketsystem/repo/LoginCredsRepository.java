package de.lorenz.ticketsystem.repo;

import de.lorenz.ticketsystem.entity.ApiLoginCreds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginCredsRepository extends JpaRepository<ApiLoginCreds, Long> {

    Optional<ApiLoginCreds> findByEmailAndClientIdAndClientSecret(String email, String clientId, String clientSecret);

}

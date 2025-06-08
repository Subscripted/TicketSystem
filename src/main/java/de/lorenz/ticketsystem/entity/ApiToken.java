package de.lorenz.ticketsystem.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "API_Tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String email;
    String token;
    LocalDateTime expiresAt;


    @Column(name = "insert_date")
    LocalDateTime insertDate;
    @Column(name = "last_updated")
    LocalDateTime lastUpdated;


    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.insertDate = now;
        this.lastUpdated = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
}

package de.lorenz.ticketsystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "logincreds")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiLoginCreds {

    @Getter
    @Setter
    @Id
    @Column(name = "email", nullable = false)
    String email;

    @Getter
    @Setter
    @Column(name = "client_id", nullable = false)
    String clientId;

    @Getter
    @Setter
    @Column(name = "client_secret", nullable = false)
    String clientSecret;

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

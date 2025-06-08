package de.lorenz.ticketsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "ticket_user")
public class TicketUser {


    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @Column(length = 20, nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(name = "short_name", length = 2, nullable = false)
    private String shortName;

    @Column(name = "date_created", nullable = false)
    LocalDateTime insertDate;

    @Column(name = "date_last_updated", nullable = false)
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
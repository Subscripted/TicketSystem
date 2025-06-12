package de.lorenz.ticketsystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketTime {

    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "ticket_user_id", nullable = false)
    TicketUser user;

    @Column(name = "time")
    Integer time;

    @Column(name = "date_insert", nullable = false)
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

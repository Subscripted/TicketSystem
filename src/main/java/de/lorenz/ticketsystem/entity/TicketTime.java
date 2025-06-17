package de.lorenz.ticketsystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_times")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long insertId;

    @OneToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "ticket_user_id", nullable = false)
    TicketUser user;

    @Column(name = "time", nullable = false)
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
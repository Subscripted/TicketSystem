package de.lorenz.ticketsystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "ticket")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String title;
    int type;
    int status;
    String notiz;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    TicketUser assignedUser;

    @ManyToOne
    @JoinColumn(name = "assigned_tester_id")
    TicketUser assignedTester;

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
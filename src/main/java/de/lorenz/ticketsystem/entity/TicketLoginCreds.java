package de.lorenz.ticketsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "Ticket_Login_Creds")
public class TicketLoginCreds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Setter
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "date_insert", nullable = false)
    private LocalDateTime date_insert;

    @Column(name = "date_last_updated", nullable = false)
    private LocalDateTime date_last_updated;

    @OneToOne(mappedBy = "loginCreds", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private TicketUser ticketUser;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.date_insert = now;
        this.date_last_updated = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.date_last_updated = LocalDateTime.now();
    }
}
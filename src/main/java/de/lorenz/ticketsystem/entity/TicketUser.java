package de.lorenz.ticketsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ticket_user")
public class TicketUser {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private TicketLoginCreds loginCreds;

    @Column(nullable = false)
    private String name;

    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Column(name = "date_insert", nullable = false)
    private LocalDateTime date_insert;

    @Column(name = "date_last_updated", nullable = false)
    private LocalDateTime date_last_updated;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime date_created;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.date_insert = now;
        this.date_last_updated = now;
        this.date_created = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.date_last_updated = LocalDateTime.now();
    }
}
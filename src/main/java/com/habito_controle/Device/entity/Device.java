package com.habito_controle.Device.entity;

import com.habito_controle.User.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "dispositivos")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @Column(name = "nome_dispositivo", nullable = false, length = 100)
    private String name;

    @Column(name = "token_notificacao", nullable = false)
    private String notificationToken;

    @Column(name = "data_ultimo_acesso", nullable = false)
    private LocalDateTime lastAccessAt;

    @Column(name = "ativo")
    private boolean active = true;

    @PrePersist
    protected void onCreate() {
        if (this.lastAccessAt == null) {
            this.lastAccessAt = LocalDateTime.now();
        }
    }

    public UUID getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNotificationToken() { return notificationToken; }
    public void setNotificationToken(String notificationToken) { this.notificationToken = notificationToken; }

    public LocalDateTime getLastAccessAt() { return lastAccessAt; }
    public void setLastAccessAt(LocalDateTime lastAccessAt) { this.lastAccessAt = lastAccessAt; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

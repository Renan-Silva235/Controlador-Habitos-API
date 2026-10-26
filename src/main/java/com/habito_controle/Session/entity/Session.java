package com.habito_controle.Session.entity;

import com.habito_controle.Device.entity.Device;
import com.habito_controle.User.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessoes")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dispositivo_id", nullable = false)
    private Device device;

    @Column(name = "refresh_token_hash", nullable = false, unique = true)
    private String refreshTokenHash;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "data_expiracao", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "data_ultimo_acesso")
    private LocalDateTime lastAccessAt;

    @Column(name = "ativo", nullable = false)
    private boolean active = true;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Device getDevice() { return device; }
    public void setDevice(Device device) { this.device = device; }

    public String getRefreshTokenHash() { return refreshTokenHash; }
    public void setRefreshTokenHash(String refreshTokenHash) { this.refreshTokenHash = refreshTokenHash; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getLastAccessAt() { return lastAccessAt; }
    public void setLastAccessAt(LocalDateTime lastAccessAt) { this.lastAccessAt = lastAccessAt; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

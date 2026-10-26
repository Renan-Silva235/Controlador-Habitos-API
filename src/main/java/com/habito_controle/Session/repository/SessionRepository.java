package com.habito_controle.Session.repository;

import com.habito_controle.Session.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    Optional<Session> findByRefreshTokenHashAndActiveTrue(String refreshTokenHash);
}

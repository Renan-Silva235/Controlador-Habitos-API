package com.habito_controle.Auth.repository;

import com.habito_controle.Auth.entity.VerificationCode;
import com.habito_controle.Common.enums.VerificationCodeType;
import com.habito_controle.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID> {

    Optional<VerificationCode> findByUserAndTypeAndActiveTrue(User user, VerificationCodeType type);
}
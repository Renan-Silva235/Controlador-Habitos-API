package com.habito_controle.Auth.service;

import com.habito_controle.Auth.entity.VerificationCode;
import com.habito_controle.Auth.repository.VerificationCodeRepository;
import com.habito_controle.Common.enums.VerificationCodeType;
import com.habito_controle.Exceptions.ApiException;
import com.habito_controle.Exceptions.BusinessException;
import com.habito_controle.Exceptions.TooManyAttemptsException;
import com.habito_controle.User.entity.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class VerificationCodeService {
    private static final int CODE_EXPIRARION_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 5;
    private final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public String generateCode(User user, VerificationCodeType type){
        verificationCodeRepository.findByUserAndTypeAndActiveTrue(user, type)
                .ifPresent(previousCode -> {
                    previousCode.setActive(false);
                    verificationCodeRepository.save(previousCode);
                });

        String plainCode = String.format("%06d", secureRandom.nextInt(1_000_000));
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUser(user);
        verificationCode.setType(type);
        verificationCode.setCodeHash(passwordEncoder.encode(plainCode));
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(CODE_EXPIRARION_MINUTES));
        verificationCodeRepository.save(verificationCode);
        return plainCode;
    }

    @Transactional(noRollbackFor = ApiException.class)
    public void validateCode(User user, VerificationCodeType type, String plainCode){
        VerificationCode verificationCode = verificationCodeRepository
                .findByUserAndTypeAndActiveTrue(user, type)
                .orElseThrow(() -> new BusinessException("Código Incorreto."));

        if(verificationCode.getExpiresAt().isBefore(LocalDateTime.now())){
            verificationCode.setActive(false);
            verificationCodeRepository.save(verificationCode);
            throw new BusinessException("Este código expirou.");
        }

        if(verificationCode.getAttempts() >= MAX_ATTEMPTS) {
            verificationCode.setActive(false);
            verificationCodeRepository.save(verificationCode);
            throw new TooManyAttemptsException("Muitas tentativas incorretas. Solicite um novo código.");
        }

        if(!passwordEncoder.matches(plainCode, verificationCode.getCodeHash())){
            verificationCode.setAttempts(verificationCode.getAttempts() + 1);
            verificationCodeRepository.save(verificationCode);
            throw new BusinessException("Código incorreto.");
        }

        verificationCode.setActive(false);
        verificationCodeRepository.save(verificationCode);
    }

}

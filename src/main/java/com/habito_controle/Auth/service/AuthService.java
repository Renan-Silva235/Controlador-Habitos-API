package com.habito_controle.Auth.service;

import com.habito_controle.Auth.dto.request.VerifyEmailDto;
import com.habito_controle.Common.enums.VerificationCodeType;
import com.habito_controle.Email.service.EmailService;
import com.habito_controle.Exceptions.ApiException;
import com.habito_controle.Exceptions.BusinessException;
import com.habito_controle.User.dto.request.ResendCodeDto;
import com.habito_controle.User.entity.User;
import com.habito_controle.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private EmailService emailService;

    @Transactional(noRollbackFor = ApiException.class)
    public void verifyEmail(VerifyEmailDto verifyEmailDto){
        User user = userRepository.findByEmail(verifyEmailDto.email()).orElseThrow(()-> new BusinessException("Código Incorreto."));
        verificationCodeService.validateCode(user, VerificationCodeType.EMAIL_VERIFICATION, verifyEmailDto.code());
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    @Transactional
    public void resendCode(ResendCodeDto resendCodeDto){
        Optional<User> optionalUser = userRepository.findByEmail(resendCodeDto.email());
        if(optionalUser.isEmpty() || optionalUser.get().isEmailVerified()){
            return;
        }

        User user = optionalUser.get();
        String code = verificationCodeService.generateCode(user, VerificationCodeType.EMAIL_VERIFICATION);
        emailService.sendVerificationCode(user.getEmail(), code);
    }
}

package com.habito_controle.User.service;

import com.habito_controle.Auth.service.VerificationCodeService;
import com.habito_controle.Common.enums.VerificationCodeType;
import com.habito_controle.Email.service.EmailService;
import com.habito_controle.Exceptions.EmailAlreadyExistsException;
import com.habito_controle.User.dto.request.UserRegisterDto;
import com.habito_controle.User.entity.User;
import com.habito_controle.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EmailService emailService;

    public void registerUser(UserRegisterDto userRegisterDto){
        Optional<User> emailAlreadyExist = userRepository.findByEmail(userRegisterDto.email());

        if(emailAlreadyExist.isPresent()){
            throw new EmailAlreadyExistsException("Já existe uma conta vinculada com esse e-mail.");
        }

        String passwordHash = this.passwordEncoder.encode(userRegisterDto.password());

        User newUser = new User();
        newUser.setName(userRegisterDto.name());
        newUser.setEmail(userRegisterDto.email());
        newUser.setPassword(passwordHash);

        this.userRepository.save(newUser);

        String code = verificationCodeService.generateCode(newUser, VerificationCodeType.EMAIL_VERIFICATION);
        emailService.sendVerificationCode(newUser.getEmail(), code);
    }
}

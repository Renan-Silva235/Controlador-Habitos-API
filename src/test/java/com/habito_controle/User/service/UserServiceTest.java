package com.habito_controle.User.service;

import com.habito_controle.Auth.service.VerificationCodeService;
import com.habito_controle.Common.enums.VerificationCodeType;
import com.habito_controle.Email.service.EmailService;
import com.habito_controle.Exceptions.EmailAlreadyExistsException;
import com.habito_controle.User.dto.request.UserRegisterDto;
import com.habito_controle.User.entity.User;
import com.habito_controle.User.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private VerificationCodeService verificationCodeService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve registrar um usuário se o e-mail não existir.")
    void shouldRegisterUserSuccessfully(){
        UserRegisterDto userRegisterDto = new UserRegisterDto("Renan", "renan@email.com", "senha123");

        when(userRepository.findByEmail(userRegisterDto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRegisterDto.password())).thenReturn("hash_senha_123");
        when(verificationCodeService.generateCode(any(User.class), eq(VerificationCodeType.EMAIL_VERIFICATION))).thenReturn("123456");

        userService.registerUser(userRegisterDto);

        verify(userRepository, times(1)).findByEmail(userRegisterDto.email());
        verify(passwordEncoder, times(1)).encode(userRegisterDto.password());
        verify(userRepository, times(1)).save(any(User.class));
        verify(verificationCodeService, times(1)).generateCode(any(User.class), eq(VerificationCodeType.EMAIL_VERIFICATION));
        verify(emailService, times(1)).sendVerificationCode(eq(userRegisterDto.email()), eq("123456"));
    }

    @Test
    @DisplayName("Verifica se o e-mail existe funciona.")
    void shouldRegisterUserFail(){
        UserRegisterDto userRegisterDto = new UserRegisterDto("re", "renan@email.com", "123");

        when(userRepository.findByEmail(userRegisterDto.email())).thenReturn(Optional.of(new User()));

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.registerUser(userRegisterDto);
        });

        assertEquals("Já existe uma conta vinculada com esse e-mail.", exception.getMessage());

        verify(userRepository, never()).save(any());
        verify(userRepository, times(1)).findByEmail(userRegisterDto.email());

    }

}

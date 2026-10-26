package com.habito_controle.Auth.service;

import com.habito_controle.Auth.dto.request.LoginDto;
import com.habito_controle.Auth.dto.request.ResendCodeDto;
import com.habito_controle.Auth.dto.request.VerifyEmailDto;
import com.habito_controle.Auth.dto.response.LoginResponseDto;
import com.habito_controle.Common.enums.VerificationCodeType;
import com.habito_controle.Device.entity.Device;
import com.habito_controle.Device.repository.DeviceRepository;
import com.habito_controle.Email.service.EmailService;
import com.habito_controle.Exceptions.ApiException;
import com.habito_controle.Exceptions.BusinessException;
import com.habito_controle.Exceptions.TooManyAttemptsException;
import com.habito_controle.Exceptions.UnauthorizedException;
import com.habito_controle.Security.JwtService;
import com.habito_controle.Session.entity.Session;
import com.habito_controle.Session.repository.SessionRepository;
import com.habito_controle.User.entity.User;
import com.habito_controle.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int LOCK_MINUTES = 5;
    private static final String TOO_MANY_ATTEMPTS_MESSAGE = "Muitas tentativas incorretas. Tente novamente em 5 minutos.";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Value("${jwt.refresh-expiration-days}")
    private long refreshExpirationDays;

    @Transactional(noRollbackFor = ApiException.class)
    public void verifyEmail(VerifyEmailDto verifyEmailDto) {
        User user = userRepository.findByEmail(verifyEmailDto.email())
                .orElseThrow(() -> new BusinessException("Código incorreto."));

        verificationCodeService.validateCode(user, VerificationCodeType.EMAIL_VERIFICATION, verifyEmailDto.code());

        user.setEmailVerified(true);
        userRepository.save(user);
    }

    @Transactional
    public void resendCode(ResendCodeDto resendCodeDto) {
        Optional<User> optionalUser = userRepository.findByEmail(resendCodeDto.email());

        if (optionalUser.isEmpty() || optionalUser.get().isEmailVerified()) {
            return;
        }

        User user = optionalUser.get();
        String code = verificationCodeService.generateCode(user, VerificationCodeType.EMAIL_VERIFICATION);
        emailService.sendVerificationCode(user.getEmail(), code);
    }

    @Transactional(noRollbackFor = ApiException.class)
    public LoginResponseDto login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new UnauthorizedException("E-mail ou senha incorretos."));

        LocalDateTime now = LocalDateTime.now();

        if (user.getLockedUntil() != null) {
            if (user.getLockedUntil().isAfter(now)) {
                throw new TooManyAttemptsException(TOO_MANY_ATTEMPTS_MESSAGE);
            }
            user.setLockedUntil(null);
            user.setLoginAttempts(0);
        }

        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            user.setLoginAttempts(user.getLoginAttempts() + 1);

            if (user.getLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
                user.setLockedUntil(now.plusMinutes(LOCK_MINUTES));
                user.setLoginAttempts(0);
                userRepository.save(user);
                throw new TooManyAttemptsException(TOO_MANY_ATTEMPTS_MESSAGE);
            }

            userRepository.save(user);
            throw new UnauthorizedException("E-mail ou senha incorretos.");
        }

        if (!user.isEmailVerified()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Verifique seu e-mail antes de entrar.");
        }

        user.setLoginAttempts(0);
        user.setLockedUntil(null);
        userRepository.save(user);

        Device device = new Device();
        device.setUser(user);
        device.setName(loginDto.deviceName());
        device.setNotificationToken(loginDto.notificationToken());
        deviceRepository.save(device);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Session session = new Session();
        session.setUser(user);
        session.setDevice(device);
        session.setRefreshTokenHash(jwtService.hashToken(refreshToken));
        session.setExpiresAt(now.plusDays(refreshExpirationDays));
        session.setLastAccessAt(now);
        sessionRepository.save(session);

        return new LoginResponseDto(accessToken, refreshToken);
    }
}
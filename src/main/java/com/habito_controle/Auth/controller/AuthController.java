package com.habito_controle.Auth.controller;

import com.habito_controle.Auth.dto.request.LoginDto;
import com.habito_controle.Auth.dto.request.RefreshTokenDto;
import com.habito_controle.Auth.dto.request.ResendCodeDto;
import com.habito_controle.Auth.dto.request.VerifyEmailDto;
import com.habito_controle.Auth.dto.response.LoginResponseDto;
import com.habito_controle.Auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestBody @Valid VerifyEmailDto verifyEmailDto) {
        authService.verifyEmail(verifyEmailDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-code")
    public ResponseEntity<Void> resendCode(@RequestBody @Valid ResendCodeDto resendCodeDto) {
        authService.resendCode(resendCodeDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody @Valid RefreshTokenDto refreshTokenDto) {
        return ResponseEntity.ok(authService.refresh(refreshTokenDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenDto refreshTokenDto) {
        authService.logout(refreshTokenDto);
        return ResponseEntity.ok().build();
    }
}
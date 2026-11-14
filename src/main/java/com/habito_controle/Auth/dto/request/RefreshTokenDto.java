package com.habito_controle.Auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDto(
        @NotBlank(message = "O campo refresh token é obrigatório.")
        String refreshToken
) {}

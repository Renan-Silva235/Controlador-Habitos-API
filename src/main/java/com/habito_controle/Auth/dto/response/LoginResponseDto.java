package com.habito_controle.Auth.dto.response;

public record LoginResponseDto(
        String accessToken,
        String refreshToken
) {}
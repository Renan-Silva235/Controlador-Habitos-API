package com.habito_controle.Auth.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResendCodeDto(
        @NotBlank(message = "O campo e-mail é obrigatório.")
        @Email(message = "Insira um e-mail válido.")
        String email
) {}
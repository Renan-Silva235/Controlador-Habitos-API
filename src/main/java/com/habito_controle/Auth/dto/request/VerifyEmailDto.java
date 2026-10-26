package com.habito_controle.Auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyEmailDto(
        @NotBlank(message = "O campo e-mail é obrigatório.")
        @Email(message = "Insira um e-mail válido.")
        String email,

        @NotBlank(message = "O campo código é obrigatório.")
        @Pattern(regexp = "\\d{6}", message = "O código deve ter 6 dígitos.")
        String code
) {
}

package com.habito_controle.Auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDto(
        @NotBlank(message = "O campo e-mail é obrigatório.")
        @Email(message = "Insira um e-mail válido.")
        String email,

        @NotBlank(message = "O campo senha é obrigatório.")
        String password,

        @NotBlank(message = "O campo nome do dispositivo é obrigatório.")
        @Size(max = 100, message = "O nome do dispositivo deve ter no máximo 100 caracteres.")
        String deviceName,

        @NotBlank(message = "O campo token de notificação é obrigatório.")
        String notificationToken
) {}

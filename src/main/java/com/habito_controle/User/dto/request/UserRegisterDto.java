package com.habito_controle.User.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record UserRegisterDto(
        @NotBlank(message = "O campo nome é obrigatório.")
        @Size(min = 3, max = 100, message = "Nome deve ter no mínimo 3 caracteres e no máximo 100 caracteres.")
        String name,

        @Email(message = "Insira um e-mail válido.")
        @NotBlank(message = "O campo email é obrigatório.")
        String email,

        @NotBlank(message = "O campo email é obrigatório.")
        @Size(min = 6, message = "O campo senha deve ter no mínimo 6 caracteres.")
        String password
) {}

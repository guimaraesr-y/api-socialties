package br.com.socialties.domain.authentication.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "Email field should not be blank")
        String email,

        @NotBlank(message = "Password field should not be blank")
        String password
) {}

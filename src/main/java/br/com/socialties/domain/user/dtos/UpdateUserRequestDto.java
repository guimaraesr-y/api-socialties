package br.com.socialties.domain.user.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record UpdateUserRequestDto(

        @NotBlank(message = "Name cannot be blank")
        String name,
        Optional<String> password

) {}

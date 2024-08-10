package br.com.socialties.domain.authentication.dtos;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public record RegisterRequestDto (

        @NotBlank(message = "Name field should not be blank")
        String name,

        @NotBlank(message = "Email field should not be blank")
        String email,

        @NotBlank(message = "Password field should not be blank")
        String password,

        Optional<MultipartFile> profilePicture

) {}

package br.com.socialties.domain.user.dtos;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public record UpdateUserRequestDto(

        Optional<String> name,
        Optional<String> password,
        Optional<MultipartFile> profilePicture

) {}

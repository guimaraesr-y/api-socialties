package br.com.socialties.domain.post.dtos;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreatePostRequestDto(

        String title,
        String description,
        List<MultipartFile> contents

) {}

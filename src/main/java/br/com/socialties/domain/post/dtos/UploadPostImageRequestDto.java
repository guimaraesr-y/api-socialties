package br.com.socialties.domain.post.dtos;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UploadPostImageRequestDto (

        String id,
        List<MultipartFile> contents

) {}

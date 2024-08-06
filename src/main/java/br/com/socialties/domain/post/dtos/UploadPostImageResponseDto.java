package br.com.socialties.domain.post.dtos;

import java.util.List;

public record UploadPostImageResponseDto (

        List<String> contentPaths

) {}

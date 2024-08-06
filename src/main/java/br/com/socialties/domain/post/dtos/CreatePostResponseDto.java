package br.com.socialties.domain.post.dtos;

import java.util.List;

public record CreatePostResponseDto(

        String id,
        String title,
        String description,
        List<String> contentPaths

) {}

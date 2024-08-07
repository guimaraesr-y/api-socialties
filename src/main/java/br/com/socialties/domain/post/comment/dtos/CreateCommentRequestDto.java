package br.com.socialties.domain.post.comment.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequestDto (

        @NotBlank
        String text

) {}

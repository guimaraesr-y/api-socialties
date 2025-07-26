package br.com.socialties.domain.comment.dtos;

import br.com.socialties.domain.comment.Comment;

public record CreateCommentResponseDto (

        String id,
        String text

) {
    public static CreateCommentResponseDto fromComment(Comment comment) {
        return new CreateCommentResponseDto(comment.getId(), comment.getText());
    }
}

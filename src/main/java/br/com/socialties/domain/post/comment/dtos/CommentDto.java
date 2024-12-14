package br.com.socialties.domain.post.comment.dtos;

import br.com.socialties.domain.post.comment.Comment;
import br.com.socialties.domain.post.dtos.PostDto;
import br.com.socialties.domain.user.dtos.UserDto;

public record CommentDto (
        String id,
        String text,
        Integer likesCount,
        Integer dislikesCount,
        UserDto user,
        PostDto post
) {

    public static CommentDto fromComment(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getLikesCount(),
                comment.getDislikesCount(),
                UserDto.fromUser(comment.getAuthor()),
                PostDto.fromPost(comment.getPost())
        );
    }
}

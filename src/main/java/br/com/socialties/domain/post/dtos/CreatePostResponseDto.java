package br.com.socialties.domain.post.dtos;

import br.com.socialties.domain.post.Post;

import java.util.List;

public record CreatePostResponseDto(

        String id,
        String title,
        String description,
        List<String> contentPaths

) {
    public static CreatePostResponseDto fromPost(Post post) {
        return new CreatePostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getContentPaths()
        );
    }
}

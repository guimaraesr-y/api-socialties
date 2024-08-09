package br.com.socialties.domain.post.dtos;

import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.user.dtos.UserNoRelationshipDto;

import java.util.List;

public record PostDto (

    String id,
    String title,
    String description,
    List<String> contentPaths,
    UserNoRelationshipDto author

) {

    public static PostDto fromPost(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getContentPaths(),
                UserNoRelationshipDto.fromUser(post.getAuthor())
        );
    }
}

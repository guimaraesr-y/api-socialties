package br.com.socialties.domain.user.dtos;

import br.com.socialties.domain.user.User;

public record UserNoRelationshipDto (

        String id,
        String name,
        String email,
        Integer numFollowers,
        Integer numFollowing

) {
    public static UserNoRelationshipDto fromUser(User user) {
        return new UserNoRelationshipDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getNumFollowers(),
                user.getNumFollowing()
        );
    }
}

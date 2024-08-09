package br.com.socialties.domain.user.dtos;

import br.com.socialties.domain.user.User;

import java.util.List;

public record UserDto(

        String id,
        String name,
        String email,
        Integer numFollowers,
        Integer numFollowing,
        List<UserNoRelationshipDto> followers,
        List<UserNoRelationshipDto> following

) {
    public static UserDto fromUser(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getNumFollowers(),
                user.getNumFollowing(),
                user.getFollowers().stream().map(UserNoRelationshipDto::fromUser).toList(),
                user.getFollowing().stream().map(UserNoRelationshipDto::fromUser).toList()
        );
    }
}

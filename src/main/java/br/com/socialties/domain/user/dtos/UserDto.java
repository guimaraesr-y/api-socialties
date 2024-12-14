package br.com.socialties.domain.user.dtos;

import br.com.socialties.domain.user.User;

import java.util.List;

public record UserDto(

        String id,
        String name,
        String email,
        String profilePicturePath,
        Boolean isPublic,
        Integer numFollowers,
        Integer numFollowing

) {
    public static UserDto fromUser(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfilePicturePath(),
                user.getIsPublic(),
                user.getNumFollowers(),
                user.getNumFollowing()
        );
    }
}

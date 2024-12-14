package br.com.socialties.domain.user.dtos;

import br.com.socialties.domain.user.User;

public record PrivateUserDto (
        String id,
        String name,
        String profilePicturePath,
        Boolean isPublic,
        Integer numFollowers,
        Integer numFollowing
) {

    public static PrivateUserDto fromUser(User user) {
        return new PrivateUserDto(
                user.getId(),
                user.getName(),
                user.getProfilePicturePath(),
                user.getIsPublic(),
                user.getNumFollowers(),
                user.getNumFollowing()
        );
    }

    public static PrivateUserDto fromUserDto(User user) {
        return new PrivateUserDto(
                user.getId(),
                user.getName(),
                user.getProfilePicturePath(),
                user.getIsPublic(),
                user.getNumFollowers(),
                user.getNumFollowing()
        );
    }

}

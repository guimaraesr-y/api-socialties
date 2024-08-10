package br.com.socialties.domain.authentication.dtos;

import br.com.socialties.domain.user.User;

public record RegisterResponseDto (

        String id,
        String name,
        String email,
        String profilePicturePath

) {

    public static RegisterResponseDto fromUser(User user) {
        return new RegisterResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfilePicturePath()
        );
    }

}

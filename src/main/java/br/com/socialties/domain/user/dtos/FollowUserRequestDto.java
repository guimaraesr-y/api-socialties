package br.com.socialties.domain.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record FollowUserRequestDto(
        @NotBlank(message = "Target user id cannot be blank")
        String userId
) {
}

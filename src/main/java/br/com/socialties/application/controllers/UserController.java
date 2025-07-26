package br.com.socialties.application.controllers;

import br.com.socialties.application.usecases.user.*;
import br.com.socialties.domain.user.authorizations.UserAuthorization;
import br.com.socialties.domain.user.dtos.FollowUserRequestDto;
import br.com.socialties.domain.user.dtos.PrivateUserDto;
import br.com.socialties.domain.user.dtos.UpdateUserRequestDto;
import br.com.socialties.domain.user.dtos.UserDto;
import br.com.socialties.helpers.controllers.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserAuthorization userAuthorization;
    private final FindUserUseCase findUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final ListUserFollowingUseCase listUserFollowingUseCase;
    private final ListUserFollowersUseCase listUserFollowersUseCase;
    private final ListUserFollowRequestsUseCase listUserFollowRequestsUseCase;
    private final FollowUserUseCase followUserUseCase;
    private final UnfollowUserUseCase unfollowUserUseCase;
    private final AcceptFollowRequestUseCase acceptFollowRequestUseCase;
    private final RejectFollowRequestUseCase rejectFollowRequestUseCase;


    @GetMapping("/me")
    public UserDto me() {
        var loggedUser = this.getLoggedUser();
        return UserDto.fromUser(loggedUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserData(@PathVariable String userId) {
        try {
            userAuthorization.canRead(userId);
            UserDto userDto = UserDto.fromUser(findUserUseCase.execute(userId));
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            PrivateUserDto privateUserDto = PrivateUserDto.fromUser(findUserUseCase.execute(userId));
            return ResponseEntity.ok(privateUserDto);
        }
    }

    @PutMapping("/{userId}")
    @PreAuthorize("@userAuthorization.canUpdate(#userId)")
    public UserDto update(@Valid @ModelAttribute UpdateUserRequestDto updateUserRequestDto, @PathVariable String userId) {
        var loggedUser = this.getLoggedUser();
        return UserDto.fromUser(updateUserUseCase.execute(loggedUser, updateUserRequestDto));
    }

    @GetMapping("/{userId}/following")
    @PreAuthorize("@userAuthorization.canRead(#userId)")
    public List<UserDto> following(@PathVariable String userId) {
        var user = findUserUseCase.execute(userId);
        return listUserFollowingUseCase.execute(user)
                .stream().map(UserDto::fromUser).toList();
    }

    @GetMapping("/{userId}/followers")
    @PreAuthorize("@userAuthorization.canRead(#userId)")
    public List<UserDto> followers(@PathVariable String userId) {
        var user = findUserUseCase.execute(userId);
        return listUserFollowersUseCase.execute(user)
                .stream().map(UserDto::fromUser).toList();
    }

    @GetMapping("/{userId}/follow-requests")
    @PreAuthorize("@userAuthorization.isOwner(#userId)")
    public List<UserDto> followRequests(@PathVariable String userId) {
        var user = findUserUseCase.execute(userId);
        return listUserFollowRequestsUseCase.execute(user)
                .stream().map(UserDto::fromUser).toList();
    }

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@Valid @RequestBody FollowUserRequestDto followUserRequestDto) {
        var loggedUser = this.getLoggedUser();
        followUserUseCase.execute(loggedUser, followUserRequestDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Void> unfollow(@Valid @RequestBody FollowUserRequestDto followUserRequestDto) {
        var loggedUser = this.getLoggedUser();
        unfollowUserUseCase.execute(loggedUser, followUserRequestDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/follow-request/accept")
    public ResponseEntity<Void> acceptFollowRequest(@PathVariable String userId) {
        var loggedUser = this.getLoggedUser();
        acceptFollowRequestUseCase.execute(loggedUser, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/follow-request/reject")
    public ResponseEntity<Void> rejectFollowRequest(@PathVariable String userId) {
        var loggedUser = this.getLoggedUser();
        rejectFollowRequestUseCase.execute(loggedUser, userId);
        return ResponseEntity.ok().build();
    }

}

package br.com.socialties.domain.user;

import br.com.socialties.domain.user.dtos.FollowUserRequestDto;
import br.com.socialties.domain.user.dtos.UpdateUserRequestDto;
import br.com.socialties.domain.user.dtos.UserDto;
import br.com.socialties.domain.user.dtos.UserNoRelationshipDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDto me(Principal principal) {
        var user = userService.findUser((User) ((Authentication) principal).getPrincipal());
        return UserDto.fromUser(user);
    }

    @GetMapping("/{userId}")
    public UserDto getUserData(@PathVariable String userId) {
        return UserDto.fromUser(userService.findUser(userId));
    }

    @PutMapping
    public UserDto update(@Valid @ModelAttribute UpdateUserRequestDto updateUserRequestDto, Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        return UserDto.fromUser(userService.updateUser(loggedUser, updateUserRequestDto));
    }

    @GetMapping("/following")
    public List<UserNoRelationshipDto> following(Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        return userService.getFollowing(loggedUser)
                .stream().map(UserNoRelationshipDto::fromUser).toList();
    }

    @GetMapping("/followers")
    public List<UserNoRelationshipDto> followers(Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        return userService.getFollowers(loggedUser)
                .stream().map(UserNoRelationshipDto::fromUser).toList();
    }

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@Valid @RequestBody FollowUserRequestDto followUserRequestDto, Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        userService.follow(loggedUser, followUserRequestDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Void> unfollow(@Valid @RequestBody FollowUserRequestDto followUserRequestDto, Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        userService.unfollow(loggedUser, followUserRequestDto);

        return ResponseEntity.ok().build();
    }

}

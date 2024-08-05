package br.com.socialties.domain.user;

import br.com.socialties.domain.user.dtos.FollowUserRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
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
    public User me(Principal principal) {
        return userService.findUser((User) ((Authentication) principal).getPrincipal());
    }

    @GetMapping("/following")
    public List<User> following(Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        Hibernate.initialize(loggedUser.getFollowing());
        return userService.getFollowing(loggedUser);
    }

    @GetMapping("/followers")
    public List<User> followers(Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        return userService.getFollowers(loggedUser);
    }

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@Valid @RequestBody FollowUserRequestDto followUserRequestDto, Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        userService.follow(loggedUser, followUserRequestDto.userId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Void> unfollow(@Valid @RequestBody FollowUserRequestDto followUserRequestDto, Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        userService.unfollow(loggedUser, followUserRequestDto.userId());

        return ResponseEntity.ok().build();
    }

}

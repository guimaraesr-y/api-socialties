package br.com.socialties.user;

import br.com.socialties.domain.authentication.AuthService;
import br.com.socialties.domain.authentication.dtos.RegisterRequestDto;
import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserService;
import br.com.socialties.domain.user.dtos.FollowUserRequestDto;
import br.com.socialties.domain.user.dtos.UpdateUserRequestDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    private User john;
    private User jane;

    @BeforeEach
    public void setup() {
        try {
            john = authService.register(
                    new RegisterRequestDto("John Doe", "johndoe@exameple.com", "password")
            );
            jane = authService.register(
                    new RegisterRequestDto("Jane Doe", "janedoe@exameple.com", "password")
            );

            userService.follow(john, new FollowUserRequestDto(jane.getId()));

            if (john == null || jane == null) {
                throw new RuntimeException("Failed to initialize test data.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Setup failed: " + e.getMessage());
        }
    }

    @AfterEach
    public void cleanup() {
        try {
            authService.deleteUser(john);
            authService.deleteUser(jane);

            john = null;
            jane = null;
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Cleanup failed: " + e.getMessage());
        }
    }

    @Test
    public void getFollowers() {
        var followersJohn = userService.getFollowers(john);
        var followersJane = userService.getFollowers(jane);

        Assertions.assertEquals(0, followersJohn.size());
        Assertions.assertEquals(1, followersJane.size());
    }

    @Test
    public void getFollowing() {
        var followingJohn = userService.getFollowing(john);
        var followingJane = userService.getFollowing(jane);

        Assertions.assertEquals(1, followingJohn.size());
        Assertions.assertEquals(0, followingJane.size());
    }

    @Test
    public void updateUser() {
        var updatedUser = userService.updateUser(
                john,
                new UpdateUserRequestDto("John Doe Edited", Optional.of("updatedpassword")));

        var updatedUser2 = userService.updateUser(
                john,
                new UpdateUserRequestDto("John Doe Edited 2", Optional.empty()) );

        Assertions.assertEquals("John Doe Edited 2", updatedUser.getName());
        Assertions.assertEquals(updatedUser.getPassword(), updatedUser2.getPassword());
    }

}

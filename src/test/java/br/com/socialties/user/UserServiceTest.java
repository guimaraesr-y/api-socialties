package br.com.socialties.user;

import br.com.socialties.domain.authentication.AuthService;
import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserService;
import br.com.socialties.domain.user.dtos.FollowUserRequestDto;
import br.com.socialties.domain.user.dtos.UpdateUserRequestDto;
import br.com.socialties.domain.user.exceptions.FollowYourselfException;
import br.com.socialties.user.helpers.UserTestHelper;
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

    @Autowired
    private UserTestHelper userTestHelper;

    private User john;
    private User jane;

    @BeforeEach
    public void setup() {
        john = userTestHelper.createUser();

        var janeUser = new User();
        janeUser.setName("Jane Doe");
        janeUser.setEmail("janedoe@exameple.com");
        jane = userTestHelper.createUser(janeUser);

        userService.follow(john, new FollowUserRequestDto(jane.getId()));
    }

    @AfterEach
    public void cleanup() {
        userTestHelper.tearDown();
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
                new UpdateUserRequestDto(
                        Optional.of("John Doe Edited"),
                        Optional.of("updatedpassword"),
                        Optional.empty(),
                        Optional.empty())
        );

        var updatedUser2 = userService.updateUser(
                john,
                new UpdateUserRequestDto(
                        Optional.of("John Doe Edited 2"),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty())
        );

        Assertions.assertEquals("John Doe Edited 2", updatedUser.getName());
        Assertions.assertEquals(updatedUser.getPassword(), updatedUser2.getPassword());
    }

    @Test
    public void unfollow() {
        userService.unfollow(john, new FollowUserRequestDto(jane.getId()));

        Assertions.assertEquals(0, userService.getFollowing(john).size());
        Assertions.assertEquals(0, userService.getFollowers(jane).size());
    }

    @Test
    public void followYourself() {
        Assertions.assertThrows(FollowYourselfException.class, () -> {
            userService.follow(john, new FollowUserRequestDto(john.getId()));
        });
    }

    @Test
    public void requestFollowingPrivateAccount() {
        john.unfollow(jane);
        var privateUser = userTestHelper.createPrivateUser(jane);

        john.follow(privateUser);

        Assertions.assertEquals(1, privateUser.getRequestFollowers().size());
        Assertions.assertTrue(privateUser.getRequestFollowers().contains(john));
    }

    @Test
    public void acceptFollower() {
        john.unfollow(jane);
        jane = userTestHelper.createUserWithRequestFollower(jane, john);

        userService.acceptFollower(jane, john.getId());

        Assertions.assertEquals(1, userService.getFollowers(jane).size());
        Assertions.assertEquals(0, userService.getRequestFollowers(jane).size());
    }

    @Test
    public void rejectFollower() {
        john.unfollow(jane);
        jane = userTestHelper.createUserWithRequestFollower(jane, john);

        userService.rejectFollower(jane, john.getId());

        Assertions.assertEquals(0, userService.getFollowers(jane).size());
        Assertions.assertEquals(0, userService.getRequestFollowers(jane).size());
    }

}

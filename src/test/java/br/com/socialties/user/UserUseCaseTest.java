package br.com.socialties.user;

import br.com.socialties.application.usecases.user.AcceptFollowRequestUseCase;
import br.com.socialties.application.usecases.user.FollowUserUseCase;
import br.com.socialties.application.usecases.user.ListUserFollowersUseCase;
import br.com.socialties.application.usecases.user.ListUserFollowingUseCase;
import br.com.socialties.application.usecases.user.ListUserFollowRequestsUseCase;
import br.com.socialties.application.usecases.user.RejectFollowRequestUseCase;
import br.com.socialties.application.usecases.user.UnfollowUserUseCase;
import br.com.socialties.application.usecases.user.UpdateUserUseCase;
import br.com.socialties.domain.user.User;
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
public class UserUseCaseTest {

    @Autowired
    private UserTestHelper userTestHelper;

    @Autowired
    private FollowUserUseCase followUserUseCase;

    @Autowired
    private ListUserFollowersUseCase listUserFollowersUseCase;

    @Autowired
    private ListUserFollowingUseCase listUserFollowingUseCase;

    @Autowired
    private UpdateUserUseCase updateUserUseCase;

    @Autowired
    private UnfollowUserUseCase unfollowUserUseCase;

    @Autowired
    private AcceptFollowRequestUseCase acceptFollowRequestUseCase;

    @Autowired
    private RejectFollowRequestUseCase rejectFollowRequestUseCase;

    @Autowired
    private ListUserFollowRequestsUseCase listUserFollowRequestsUseCase;

    private User john;
    private User jane;

    @BeforeEach
    public void setup() {
        john = userTestHelper.createUser();

        var janeUser = new User();
        janeUser.setName("Jane Doe");
        janeUser.setEmail("janedoe@exameple.com");
        jane = userTestHelper.createUser(janeUser);

        followUserUseCase.execute(john, new FollowUserRequestDto(jane.getId()));
    }

    @AfterEach
    public void cleanup() {
        userTestHelper.tearDown();
    }

    @Test
    public void getFollowers() {
        var followersJohn = listUserFollowersUseCase.execute(john);
        var followersJane = listUserFollowersUseCase.execute(jane);

        Assertions.assertEquals(0, followersJohn.size());
        Assertions.assertEquals(1, followersJane.size());
    }

    @Test
    public void getFollowing() {
        var followingJohn = listUserFollowingUseCase.execute(john);
        var followingJane = listUserFollowingUseCase.execute(jane);

        Assertions.assertEquals(1, followingJohn.size());
        Assertions.assertEquals(0, followingJane.size());
    }

    @Test
    public void updateUser() {
        var updatedUser = updateUserUseCase.execute(
                john,
                new UpdateUserRequestDto(
                        Optional.of("John Doe Edited"),
                        Optional.of("updatedpassword"),
                        Optional.empty(),
                        Optional.empty())
        );

        var updatedUser2 = updateUserUseCase.execute(
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
        unfollowUserUseCase.execute(john, new FollowUserRequestDto(jane.getId()));

        Assertions.assertEquals(0, listUserFollowingUseCase.execute(john).size());
        Assertions.assertEquals(0, listUserFollowersUseCase.execute(jane).size());
    }

    @Test
    public void followYourself() {
        Assertions.assertThrows(FollowYourselfException.class, () -> {
            followUserUseCase.execute(john, new FollowUserRequestDto(john.getId()));
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

        acceptFollowRequestUseCase.execute(jane, john.getId());

        Assertions.assertEquals(1, listUserFollowersUseCase.execute(jane).size());
        Assertions.assertEquals(0, listUserFollowRequestsUseCase.execute(jane).size());
    }

    @Test
    public void rejectFollower() {
        john.unfollow(jane);
        jane = userTestHelper.createUserWithRequestFollower(jane, john);

        rejectFollowRequestUseCase.execute(jane, john.getId());

        Assertions.assertEquals(0, listUserFollowersUseCase.execute(jane).size());
        Assertions.assertEquals(0, listUserFollowRequestsUseCase.execute(jane).size());
    }

}

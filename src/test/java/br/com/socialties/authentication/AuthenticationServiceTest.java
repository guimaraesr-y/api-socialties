package br.com.socialties.authentication;

import br.com.socialties.domain.authentication.AuthService;
import br.com.socialties.domain.authentication.dtos.LoginRequestDto;
import br.com.socialties.domain.authentication.dtos.RegisterRequestDto;
import br.com.socialties.domain.user.User;
import br.com.socialties.user.helpers.UserTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class AuthenticationServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserTestHelper userTestHelper;

    private User john;

    public void setup() {
        john = userTestHelper.createUser();
    }

    public void cleanup() {
        userTestHelper.tearDown();
    }

    @BeforeEach
    public void beforeEach() {
        setup();
    }

    @AfterEach
    public void afterEach() {
        cleanup();
    }

    @Test
    public void register() {
        var jane = authService.register(
                new RegisterRequestDto("Jane Doe", "janedoe@example.com", "password", Optional.empty(), Optional.empty())
        );

        Assertions.assertNotNull(jane);
    }

    @Test
    public void registerWithExistingEmail() {
        User user;
        try {
            user = authService.register(
                    new RegisterRequestDto("John Doe", john.getEmail(), "password", Optional.empty(), Optional.empty())
            );
        } catch (Exception e) {
            user = null;
        }

        Assertions.assertNull(user);
    }

    @Test
    public void correctLogin() {
        var token = authService
                .login(new LoginRequestDto(john.getEmail(), "password"));

        Assertions.assertNotNull(token);
    }

    @Test
    public void wrongLogin() {
        String token;

        try {
            token = authService
                    .login(new LoginRequestDto("janedoe@example.com", "wrongpassword"));
        } catch (Exception e) {
            token = null;
        }

        Assertions.assertNull(token);
    }

}

package br.com.socialties.authentication;

import br.com.socialties.domain.authentication.AuthService;
import br.com.socialties.domain.authentication.dtos.LoginRequestDto;
import br.com.socialties.domain.authentication.dtos.RegisterRequestDto;
import br.com.socialties.domain.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthenticationServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    public void register() {
        var john = authService.register(
                new RegisterRequestDto("John Doe", "johndoe@example.com", "password")
        );

        var jane = authService.register(
                new RegisterRequestDto("Jane Doe", "janedoe@example.com", "password")
        );

        Assertions.assertNotNull(john);
        Assertions.assertNotNull(jane);
    }

    @Test
    public void registerWithExistingEmail() {
        User john;
        try {
            john = authService.register(
                    new RegisterRequestDto("John Doe", "johndoe@example.com", "password")
            );
        } catch (Exception e) {
            john = null;
        }

        Assertions.assertNull(john);
    }

    @Test
    public void correctLogin() {
        var token = authService
                .login(new LoginRequestDto("johndoe@example.com", "password"));


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

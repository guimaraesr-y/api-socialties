package br.com.socialties.domain.authentication;

import br.com.socialties.domain.authentication.dtos.LoginRequestDto;
import br.com.socialties.domain.authentication.dtos.RegisterRequestDto;
import br.com.socialties.domain.authentication.exceptions.UserAlreadyExists;
import br.com.socialties.domain.authentication.exceptions.UserNotFoundException;
import br.com.socialties.domain.authentication.exceptions.UserPasswordMismatch;
import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserRepository;
import br.com.socialties.infra.security.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public String login(LoginRequestDto loginRequestDto) {
        var user = userRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(loginRequestDto.password(), user.getPassword())) {
            throw new UserPasswordMismatch();
        }

        return tokenService.generateToken(user);
    }

    public User register(RegisterRequestDto registerRequestDto) {
        if(userRepository.existsByEmail(registerRequestDto.email())) {
            throw new UserAlreadyExists();
        }

        var user = new User();
        user.setEmail(registerRequestDto.email());
        user.setName(registerRequestDto.name());
        user.setPassword(passwordEncoder.encode(registerRequestDto.password()));
        user.setNumFollowers(0);
        user.setNumFollowing(0);

        return userRepository.save(user);
    }

    public void deleteUser(User john) {
        userRepository.delete(john);
    }

}

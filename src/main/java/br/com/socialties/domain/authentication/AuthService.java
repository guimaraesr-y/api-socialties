package br.com.socialties.domain.authentication;

import br.com.socialties.domain.authentication.dtos.LoginRequestDto;
import br.com.socialties.domain.authentication.dtos.RegisterRequestDto;
import br.com.socialties.domain.authentication.dtos.RegisterResponseDto;
import br.com.socialties.domain.authentication.exceptions.UserAlreadyExists;
import br.com.socialties.domain.authentication.exceptions.UserNotFoundException;
import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserRepository;
import br.com.socialties.infra.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public String login(LoginRequestDto loginRequestDto) {
        var user = userRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(user.getPassword(), loginRequestDto.password())) {
            throw new RuntimeException("Passwords do not match");
        }

        return tokenService.generateToken(user);
    }

    public String register(RegisterRequestDto registerRequestDto) {
        if(userRepository.existsByEmail(registerRequestDto.email())) {
            throw new UserAlreadyExists();
        }

        var user = new User();
        user.setEmail(registerRequestDto.email());
        user.setName(registerRequestDto.name());
        user.setPassword(passwordEncoder.encode(registerRequestDto.password()));

        var registeredUser = userRepository.save(user);
        return tokenService.generateToken(registeredUser);
    }

}

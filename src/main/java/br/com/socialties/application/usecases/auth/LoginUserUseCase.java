package br.com.socialties.application.usecases.auth;

import br.com.socialties.domain.authentication.dtos.LoginRequestDto;
import br.com.socialties.domain.authentication.exceptions.UserNotFoundException;
import br.com.socialties.domain.authentication.exceptions.UserPasswordMismatch;
import br.com.socialties.domain.user.UserRepository;
import br.com.socialties.infra.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public String execute(LoginRequestDto loginRequestDto) {
        var user = userRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(loginRequestDto.password(), user.getPassword())) {
            throw new UserPasswordMismatch();
        }

        return tokenService.generateToken(user);
    }
}

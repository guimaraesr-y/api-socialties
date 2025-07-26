package br.com.socialties.application.usecases.auth;

import br.com.socialties.domain.authentication.dtos.RegisterRequestDto;
import br.com.socialties.domain.authentication.exceptions.UserAlreadyExists;
import br.com.socialties.domain.storage.StorageService;
import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    public User execute(RegisterRequestDto registerRequestDto) {
        if(userRepository.existsByEmail(registerRequestDto.email())) {
            throw new UserAlreadyExists();
        }

        var user = new User();
        user.setEmail(registerRequestDto.email());
        user.setName(registerRequestDto.name());
        user.setPassword(passwordEncoder.encode(registerRequestDto.password()));
        user.setNumFollowers(0);
        user.setNumFollowing(0);

        user.setFollowers(new ArrayList<>());
        user.setFollowing(new ArrayList<>());
        user.setPosts(new ArrayList<>());

        if(registerRequestDto.profilePicture().isPresent()) {
            var profilePicturePath = storageService
                    .store(registerRequestDto.profilePicture().get());
            user.setProfilePicturePath(profilePicturePath);
        }

        return userRepository.save(user);
    }
}

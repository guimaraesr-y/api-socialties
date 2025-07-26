package br.com.socialties.application.usecases.user;

import br.com.socialties.domain.authentication.exceptions.UserNotFoundException;
import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserUseCase {

    private final UserRepository userRepository;

    public User execute(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public User execute(User user) {
        return userRepository.findById(user.getId())
                .orElseThrow(UserNotFoundException::new);
    }
}

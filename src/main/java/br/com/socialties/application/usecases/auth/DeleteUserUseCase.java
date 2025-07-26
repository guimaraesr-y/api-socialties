package br.com.socialties.application.usecases.auth;

import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final UserRepository userRepository;

    public void execute(User user) {
        userRepository.delete(user);
    }
}

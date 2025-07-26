package br.com.socialties.application.usecases.user;

import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RejectFollowRequestUseCase {

    private final UserRepository userRepository;
    private final FindUserUseCase findUserUseCase;

    public void execute(User loggedUser, String requestFollowerId) {
        var logged = findUserUseCase.execute(loggedUser);
        var requestFollower = findUserUseCase.execute(requestFollowerId);
        logged.removeRequestFollower(requestFollower);

        userRepository.save(logged);
        userRepository.save(requestFollower);
    }
}

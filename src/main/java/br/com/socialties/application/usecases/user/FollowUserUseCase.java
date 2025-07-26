package br.com.socialties.application.usecases.user;

import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserRepository;
import br.com.socialties.domain.user.dtos.FollowUserRequestDto;
import br.com.socialties.domain.user.exceptions.FollowYourselfException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowUserUseCase {

    private final UserRepository userRepository;
    private final FindUserUseCase findUserUseCase;

    public void execute(User loggedUser, FollowUserRequestDto followUserRequestDto) {
        var logged = findUserUseCase.execute(loggedUser);
        var user = findUserUseCase.execute(followUserRequestDto.userId());

        if(user.getId().equals(logged.getId())) {
            throw new FollowYourselfException();
        }

        logged.follow(user);
        userRepository.save(logged);
        userRepository.save(user);
    }
}

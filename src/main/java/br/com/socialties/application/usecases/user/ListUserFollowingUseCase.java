package br.com.socialties.application.usecases.user;

import br.com.socialties.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListUserFollowingUseCase {

    private final FindUserUseCase findUserUseCase;

    public List<User> execute(User user) {
        var foundUser = findUserUseCase.execute(user);
        return foundUser.getFollowing();
    }
}

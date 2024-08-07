package br.com.socialties.domain.user;

import br.com.socialties.domain.authentication.exceptions.UserNotFoundException;
import br.com.socialties.domain.user.exceptions.FollowYourselfException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User findUser(User user) {
        return userRepository.findById(user.getId())
                .orElseThrow(UserNotFoundException::new);
    }

    private User findUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public List<User> getFollowers(User loggedUser) {
        var user = findUser(loggedUser);
        return user.getFollowers();
    }

    public List<User> getFollowing(User loggedUser) {
        var user = findUser(loggedUser);
        return user.getFollowing();
    }

    public void follow(User loggedUser, String userId) {
        // check if target user exists
        var user = findUser(userId);

        if(user.getId().equals(loggedUser.getId())) {
            throw new FollowYourselfException();
        }

        // check if user is already following
        if (user.getFollowers().contains(loggedUser)) {
            return;
        }

        // follow
        user.getFollowers().add(loggedUser);
        user.setNumFollowers(user.getFollowers().size());
        userRepository.save(user);
    }

    public void unfollow(User loggedUser, String userId) {
        var logged = findUser(loggedUser);
        // check if target user exists
        var user = findUser(userId);


        if(user.getId().equals(logged.getId())) {
            throw new FollowYourselfException();
        }

        // check if is following
        if (!user.getFollowers().contains(logged)) {
            return;
        }

        // unfollow
        user.getFollowers().remove(logged);
        user.setNumFollowers(user.getFollowers().size());
        userRepository.save(user);
    }
}

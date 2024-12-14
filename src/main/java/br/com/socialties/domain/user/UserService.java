package br.com.socialties.domain.user;

import br.com.socialties.domain.authentication.exceptions.UserNotFoundException;
import br.com.socialties.domain.storage.StorageService;
import br.com.socialties.domain.user.dtos.FollowUserRequestDto;
import br.com.socialties.domain.user.dtos.UpdateUserRequestDto;
import br.com.socialties.domain.user.exceptions.FollowYourselfException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

    public User findUser(User user) {
        return userRepository.findById(user.getId())
                .orElseThrow(UserNotFoundException::new);
    }

    public User findUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public User updateUser(User loggedUser, UpdateUserRequestDto updateUserRequestDto) {
        var userEdit = findUser(loggedUser);

        if(updateUserRequestDto.name().isPresent()) {
            userEdit.setName(updateUserRequestDto.name().get());
        }

        if(updateUserRequestDto.password().isPresent()) {
            var password = updateUserRequestDto.password().get();
            userEdit.setPassword(passwordEncoder.encode(passwordEncoder.encode(password)));
        }

        if(updateUserRequestDto.profilePicture().isPresent()) {
            storageService.delete(userEdit.getProfilePicturePath());
            var profilePicturePath = storageService
                    .store(updateUserRequestDto.profilePicture().get());
            userEdit.setProfilePicturePath(profilePicturePath);
        }

        return userRepository.save(userEdit);
    }

    public List<User> getFollowers(User user) {
        var foundUser = findUser(user);
        return foundUser.getFollowers();
    }

    public List<User> getFollowing(User user) {
        var foundUser = findUser(user);
        return foundUser.getFollowing();
    }

    public List<User> getRequestFollowers(User user) {
        var foundUser = findUser(user);
        return foundUser.getRequestFollowers();
    }

    public void follow(User loggedUser, FollowUserRequestDto followUserRequestDto) {
        var logged = findUser(loggedUser);
        var user = findUser(followUserRequestDto.userId());

        if(user.getId().equals(logged.getId())) {
            throw new FollowYourselfException();
        }

        logged.follow(user);
        userRepository.save(logged);
        userRepository.save(user);
    }

    public void unfollow(User loggedUser, FollowUserRequestDto followUserRequestDto) {
        var logged = findUser(loggedUser);
        var user = findUser(followUserRequestDto.userId());

        if(user.getId().equals(logged.getId())) {
            throw new FollowYourselfException();
        }

        logged.unfollow(user);
        userRepository.save(logged);
        userRepository.save(user);
    }

    public void acceptFollower(User loggedUser, String requestFollowerId) {
        var logged = findUser(loggedUser);
        var requestFollower = findUser(requestFollowerId);

        logged.acceptFollower(requestFollower);

        userRepository.save(logged);
        userRepository.save(requestFollower);
    }



}

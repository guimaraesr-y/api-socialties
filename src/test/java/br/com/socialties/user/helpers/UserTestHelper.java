package br.com.socialties.user.helpers;

import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserRepository;
import br.com.socialties.helpers.controllers.utils.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserTestHelper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User userData) {
        if(userData.getId() != null) {
            return userData;
        }

        User defaultUser = new User();
        defaultUser.setEmail("john@example.com");
        defaultUser.setName("John Doe");
        defaultUser.setPassword(passwordEncoder.encode("password"));

        ModelMapperUtil.mapNonNullProperties(userData, defaultUser);

        return userRepository.findByEmail(defaultUser.getEmail())
                .map(existingUser -> {
                    ModelMapperUtil.mapNonNullProperties(defaultUser, existingUser);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> userRepository.save(defaultUser));
    }

    public User createUser() {
        return createUser(new User());
    }

    public User createPrivateUser(User user) {
        user.setIsPublic(false);
        return createUser(user);
    }

    public User createPrivateUser() {
        return createPrivateUser(new User());
    }

    public User createUserWithRequestFollower(User user, User follower) {
        user.setIsPublic(false);
        follower.follow(user);
        return createUser(user);
    }

    public void tearDown() {
        userRepository.deleteAll();
    }

}

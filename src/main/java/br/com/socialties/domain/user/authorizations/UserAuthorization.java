package br.com.socialties.domain.user.authorizations;

import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserAuthorization {

    @Autowired
    private UserRepository userRepository;

    public boolean canUpdate(String id) {
        User logged = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userRepository.findById(id).orElse(null);
        if (user == null) return false;

        return logged.getId().equals(user.getId());
    }

}

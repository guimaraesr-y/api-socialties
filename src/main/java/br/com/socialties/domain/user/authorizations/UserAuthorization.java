package br.com.socialties.domain.user.authorizations;

import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserRepository;
import br.com.socialties.helpers.controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserAuthorization extends BaseController {

    @Autowired
    private UserRepository userRepository;

    public boolean canRead(String id) {
        User logged = this.getLoggedUser();
        var user = userRepository.findById(id).orElse(null);
        if (user == null) return false;

        return
                logged.getId().equals(user.getId()) ||  // Check if is the same user
                user.getIsPublic() ||                   // Check if user is public
                user.getFollowers().contains(logged);   // Check if user is following
    }

    public boolean canUpdate(String id) {
        User logged = this.getLoggedUser();
        var user = userRepository.findById(id).orElse(null);
        if (user == null) return false;

        return logged.getId().equals(user.getId());
    }

}

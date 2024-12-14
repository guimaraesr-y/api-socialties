package br.com.socialties.domain.user.authorizations;

import br.com.socialties.domain.authentication.exceptions.UserNotFoundException;
import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserRepository;
import br.com.socialties.domain.user.exceptions.PrivateUserException;
import br.com.socialties.domain.user.exceptions.ResourceOwnershipException;
import br.com.socialties.helpers.controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserAuthorization extends BaseController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Determines if the logged-in user has permission to read the specified user's data.
     * Throws a UserNotFoundException if the specified user does not exist.
     * Throws a PrivateUserException if the user is private and the logged-in user is not the owner,
     * the user is not public, and the logged-in user is not a follower.
     *
     * @param id the ID of the user to be accessed
     * @return true if the logged-in user can read the specified user's data
     */
    public boolean canRead(String id) {
        User logged = this.getLoggedUser();
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }

        if(
                logged.getId().equals(user.getId()) ||  // Check if is the same user
                user.getIsPublic() ||                   // Check if user is public
                user.getFollowers().contains(logged)   // Check if user is following
        ) {
            throw new PrivateUserException();
        }

        return true;

    }

    /**
     * Determines if the logged-in user has permission to update the specified user's data.
     * Throws a UserNotFoundException if the specified user does not exist.
     * Throws a ResourceOwnershipException if the logged-in user does not own the specified user.
     *
     * @param id the ID of the user to be updated
     * @return true if the logged-in user can update the specified user's data
     */
    public boolean canUpdate(String id) {
        User logged = this.getLoggedUser();

        var user = userRepository.findById(id).orElse(null);
        if(user == null) {
            throw new UserNotFoundException();
        }

        if (!logged.getId().equals(user.getId())) {
            throw new ResourceOwnershipException();
        }

        return true;
    }

}

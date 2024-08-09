package br.com.socialties.domain.user.exceptions;

import br.com.socialties.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ResourceOwnershipException extends ApplicationException {

    public ResourceOwnershipException() {
        super(HttpStatus.UNAUTHORIZED, "You cannot access this resource, because it does not belong to you");
    }

}

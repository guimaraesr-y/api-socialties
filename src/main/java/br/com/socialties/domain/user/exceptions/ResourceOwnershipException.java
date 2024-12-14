package br.com.socialties.domain.user.exceptions;

import br.com.socialties.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ResourceOwnershipException extends ApplicationException {

    public ResourceOwnershipException() {
        super(HttpStatus.UNAUTHORIZED, "You are not allowed to access this resource");
    }

}

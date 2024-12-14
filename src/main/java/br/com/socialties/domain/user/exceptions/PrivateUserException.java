package br.com.socialties.domain.user.exceptions;

import br.com.socialties.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PrivateUserException extends ApplicationException {

    public PrivateUserException() {
        super(HttpStatus.UNAUTHORIZED, "The user you are trying to access is private");
    }

}

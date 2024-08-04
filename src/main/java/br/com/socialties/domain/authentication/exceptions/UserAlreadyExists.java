package br.com.socialties.domain.authentication.exceptions;

import br.com.socialties.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExists extends ApplicationException {

    public UserAlreadyExists() {
        super(HttpStatus.CONFLICT, "Provided email is already registered");
    }

}

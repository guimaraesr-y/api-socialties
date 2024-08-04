package br.com.socialties.domain.authentication.exceptions;

import br.com.socialties.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserPasswordMismatch extends ApplicationException {

    public UserPasswordMismatch() {
        super(HttpStatus.FORBIDDEN, "Wrong password");
    }

}

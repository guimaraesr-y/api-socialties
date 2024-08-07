package br.com.socialties.infra.security.exceptions;

import br.com.socialties.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class AppSecurityException extends ApplicationException {

    public AppSecurityException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}

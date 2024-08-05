package br.com.socialties.domain.user.exceptions;

import br.com.socialties.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class FollowYourselfException extends ApplicationException {

    public FollowYourselfException() {
        super(HttpStatus.BAD_REQUEST, "You cannot follow yourself");
    }

}

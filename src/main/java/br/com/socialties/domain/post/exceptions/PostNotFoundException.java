package br.com.socialties.domain.post.exceptions;

import br.com.socialties.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PostNotFoundException extends ApplicationException {

    public PostNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Post not found");
    }

}

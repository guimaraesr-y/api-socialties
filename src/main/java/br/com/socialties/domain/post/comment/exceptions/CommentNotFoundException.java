package br.com.socialties.domain.post.comment.exceptions;

import br.com.socialties.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends ApplicationException {

    public CommentNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Comment not found");
    }
}

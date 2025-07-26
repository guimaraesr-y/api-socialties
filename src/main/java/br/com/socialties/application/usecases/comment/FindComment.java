package br.com.socialties.application.usecases.comment;

import br.com.socialties.domain.comment.Comment;
import br.com.socialties.domain.comment.CommentRepository;
import br.com.socialties.domain.comment.exceptions.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindComment {

    private final CommentRepository commentRepository;

    public Comment execute(String commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }
}

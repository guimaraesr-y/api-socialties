package br.com.socialties.application.usecases.comment;

import br.com.socialties.domain.comment.Comment;
import br.com.socialties.domain.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListCommentsByPostUseCase {

    private final CommentRepository commentRepository;

    public List<Comment> execute(String postId) {
        return commentRepository.findAllByPostId(postId);
    }

}

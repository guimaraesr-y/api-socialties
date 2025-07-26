package br.com.socialties.application.usecases.comment;

import br.com.socialties.application.usecases.user.FindUserUseCase;
import br.com.socialties.domain.comment.CommentRepository;
import br.com.socialties.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeCommentUseCase {

    private final CommentRepository commentRepository;
    private final FindCommentUseCase findCommentUseCase;
    private final FindUserUseCase findUserUseCase;

    public Boolean execute(String commentId, User loggedUser) {
        var comment = findCommentUseCase.execute(commentId);
        var user = findUserUseCase.execute(loggedUser);

        if(comment.getLikes().contains(user)) {
            comment.getLikes().remove(loggedUser);
            comment.setLikesCount(comment.getLikesCount() - 1);
            commentRepository.save(comment);
            return false;
        }

        comment.getLikes().add(loggedUser);
        comment.setLikesCount(comment.getLikesCount() + 1);
        commentRepository.save(comment);

        return true;
    }
}

package br.com.socialties.application.usecases.comment;

import br.com.socialties.application.usecases.user.FindUserUseCase;
import br.com.socialties.domain.comment.CommentRepository;
import br.com.socialties.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DislikeCommentUseCase {

    private final CommentRepository commentRepository;
    private final FindCommentUseCase findCommentUseCase;
    private final FindUserUseCase findUserUseCase;

    public Boolean execute(String commentId, User loggedUser) {
        var comment = findCommentUseCase.execute(commentId);
        var user = findUserUseCase.execute(loggedUser);

        if(comment.getDislikes().contains(user)) {
            comment.getDislikes().remove(loggedUser);
            comment.setDislikesCount(comment.getDislikesCount() - 1);
            return false;
        }

        comment.getDislikes().add(loggedUser);
        comment.setDislikesCount(comment.getDislikesCount() + 1);

        commentRepository.save(comment);
        return true;
    }
}

package br.com.socialties.application.usecases.comment;

import br.com.socialties.application.usecases.user.FindUser;
import br.com.socialties.domain.comment.CommentRepository;
import br.com.socialties.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DislikeComment {

    private final CommentRepository commentRepository;
    private final FindComment findComment;
    private final FindUser findUser;

    public Boolean execute(String commentId, User loggedUser) {
        var comment = findComment.execute(commentId);
        var user = findUser.execute(loggedUser);

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

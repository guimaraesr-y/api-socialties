package br.com.socialties.domain.post.comment;

import br.com.socialties.domain.post.PostService;
import br.com.socialties.domain.post.comment.dtos.CreateCommentRequestDto;
import br.com.socialties.domain.post.comment.exceptions.CommentNotFoundException;
import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    public Comment createComment(String postId, CreateCommentRequestDto createCommentRequestDto, User loggedUser) {
        var user = userService.findUser(loggedUser);
        var post = postService.findPost(postId);

        Comment comment = new Comment();
        comment.setText(createCommentRequestDto.text());
        comment.setAuthor(user);
        comment.setPost(post);

        comment.setLikes(new ArrayList<>());
        comment.setDislikes(new ArrayList<>());
        comment.setLikesCount(0);
        comment.setDislikesCount(0);

        commentRepository.save(comment);
        return comment;
    }

    public Boolean likeComment(String commentId, User loggedUser) {
        var comment = findComment(commentId);

        // finds the full user object from the logged user
        var user = userService.findUser(loggedUser);

        // check if the user already liked the post and remove the like
        if(comment.getLikes().contains(user)) {
            comment.getLikes().remove(loggedUser);
            comment.setLikesCount(comment.getLikesCount() - 1);
            return false;
        }

        comment.getLikes().add(loggedUser);
        comment.setLikesCount(comment.getLikesCount() + 1);

        commentRepository.save(comment);
        return true;
    }

    public Boolean dislikeComment(String commentId, User loggedUser) {
        var comment = findComment(commentId);

        // finds the full user object from the logged user
        var user = userService.findUser(loggedUser);

        // check if the user already disliked the post and remove the dislike
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

    public Comment findComment(String commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }

}

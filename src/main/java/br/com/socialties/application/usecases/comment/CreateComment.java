package br.com.socialties.application.usecases.comment;

import br.com.socialties.application.usecases.post.FindPost;
import br.com.socialties.application.usecases.user.FindUser;
import br.com.socialties.domain.comment.Comment;
import br.com.socialties.domain.comment.CommentRepository;
import br.com.socialties.domain.comment.dtos.CreateCommentRequestDto;
import br.com.socialties.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateComment {

    private final CommentRepository commentRepository;
    private final FindUser findUser;
    private final FindPost findPost;

    public Comment execute(String postId, CreateCommentRequestDto createCommentRequestDto, User loggedUser) {
        var user = findUser.execute(loggedUser);
        var post = findPost.execute(postId);

        Comment comment = new Comment();
        comment.setText(createCommentRequestDto.text());
        comment.setAuthor(user);
        comment.setPost(post);

        post.setCommentsCount(post.getCommentsCount() + 1);
        post.getComments().add(comment);

        return commentRepository.save(comment);
    }
}

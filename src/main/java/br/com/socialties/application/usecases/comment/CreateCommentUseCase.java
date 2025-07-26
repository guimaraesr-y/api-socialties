package br.com.socialties.application.usecases.comment;

import br.com.socialties.application.usecases.post.FindPostUseCase;
import br.com.socialties.application.usecases.user.FindUserUseCase;
import br.com.socialties.domain.comment.Comment;
import br.com.socialties.domain.comment.CommentRepository;
import br.com.socialties.domain.comment.dtos.CreateCommentRequestDto;
import br.com.socialties.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCommentUseCase {

    private final CommentRepository commentRepository;
    private final FindUserUseCase findUserUseCase;
    private final FindPostUseCase findPostUseCase;

    public Comment execute(String postId, CreateCommentRequestDto createCommentRequestDto, User loggedUser) {
        var user = findUserUseCase.execute(loggedUser);
        var post = findPostUseCase.execute(postId);

        Comment comment = new Comment();
        comment.setText(createCommentRequestDto.text());
        comment.setAuthor(user);
        comment.setPost(post);

        post.setCommentsCount(post.getCommentsCount() + 1);
        post.getComments().add(comment);

        return commentRepository.save(comment);
    }
}

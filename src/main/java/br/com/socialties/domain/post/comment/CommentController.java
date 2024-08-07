package br.com.socialties.domain.post.comment;

import br.com.socialties.domain.post.comment.dtos.CreateCommentRequestDto;
import br.com.socialties.domain.post.comment.dtos.CreateCommentResponseDto;
import br.com.socialties.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/post/{postId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CreateCommentResponseDto createComment(@PathVariable String postId, @RequestBody CreateCommentRequestDto createCommentRequestDto, Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        var comment = commentService.createComment(postId, createCommentRequestDto, loggedUser);

        return new CreateCommentResponseDto(comment.getId(), comment.getText());
    }



}

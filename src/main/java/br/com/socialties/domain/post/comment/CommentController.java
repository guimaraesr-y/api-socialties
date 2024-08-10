package br.com.socialties.domain.post.comment;

import br.com.socialties.domain.post.comment.dtos.CommentDto;
import br.com.socialties.domain.post.comment.dtos.CreateCommentRequestDto;
import br.com.socialties.domain.post.comment.dtos.CreateCommentResponseDto;
import br.com.socialties.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}")
    public CreateCommentResponseDto createComment(@PathVariable String postId, @RequestBody CreateCommentRequestDto createCommentRequestDto, Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        var comment = commentService.createComment(postId, createCommentRequestDto, loggedUser);

        return new CreateCommentResponseDto(comment.getId(), comment.getText());
    }

    @GetMapping("/post/{postId}")
    public List<CommentDto> getCommentsByPost(@PathVariable String postId) {
        return commentService.getCommentsByPost(postId)
                .stream().map(CommentDto::fromComment).toList();
    }

    @GetMapping("/{commentId}")
    public CommentDto findComment(@PathVariable String commentId) {
        return CommentDto.fromComment(commentService.findComment(commentId));
    }

    @PostMapping("/{commentId}/like")
    public Map<String, Boolean> likeComment(@PathVariable String commentId, Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        var liked = commentService.likeComment(commentId, loggedUser);
        return Map.of("liked", liked);
    }

    @PostMapping("/{commentId}/dislike")
    public Map<String, Boolean> dislikeComment(@PathVariable String commentId, Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        var disliked = commentService.dislikeComment(commentId, loggedUser);
        return Map.of("disliked", disliked);
    }

}

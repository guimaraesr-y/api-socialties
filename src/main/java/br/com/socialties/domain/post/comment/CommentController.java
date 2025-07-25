package br.com.socialties.domain.post.comment;

import br.com.socialties.domain.post.comment.dtos.CommentDto;
import br.com.socialties.domain.post.comment.dtos.CreateCommentRequestDto;
import br.com.socialties.domain.post.comment.dtos.CreateCommentResponseDto;
import br.com.socialties.helpers.controllers.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController extends BaseController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}")
    public CreateCommentResponseDto createComment(@PathVariable String postId, @RequestBody CreateCommentRequestDto createCommentRequestDto) {
        var loggedUser = this.getLoggedUser();
        var comment = commentService.createComment(postId, createCommentRequestDto, loggedUser);

        return CreateCommentResponseDto.fromComment(comment);
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
    public Map<String, Boolean> likeComment(@PathVariable String commentId) {
        var loggedUser = this.getLoggedUser();
        var liked = commentService.likeComment(commentId, loggedUser);
        return Map.of("liked", liked);
    }

    @PostMapping("/{commentId}/dislike")
    public Map<String, Boolean> dislikeComment(@PathVariable String commentId) {
        var loggedUser = this.getLoggedUser();
        var disliked = commentService.dislikeComment(commentId, loggedUser);
        return Map.of("disliked", disliked);
    }

}

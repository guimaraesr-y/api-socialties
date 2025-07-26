package br.com.socialties.application.controllers;

import br.com.socialties.application.usecases.comment.*;
import br.com.socialties.domain.comment.dtos.CommentDto;
import br.com.socialties.domain.comment.dtos.CreateCommentRequestDto;
import br.com.socialties.domain.comment.dtos.CreateCommentResponseDto;
import br.com.socialties.helpers.controllers.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController extends BaseController {

    private final CreateCommentUseCase createCommentUseCase;
    private final ListCommentsByPostUseCase listCommentsByPostUseCase;
    private final FindCommentUseCase findCommentUseCase;
    private final LikeCommentUseCase likeCommentUseCase;
    private final DislikeCommentUseCase dislikeCommentUseCase;

    @PostMapping("/post/{postId}")
    public CreateCommentResponseDto createComment(@PathVariable String postId, @RequestBody CreateCommentRequestDto createCommentRequestDto) {
        var loggedUser = this.getLoggedUser();
        var comment = createCommentUseCase.execute(postId, createCommentRequestDto, loggedUser);

        return CreateCommentResponseDto.fromComment(comment);
    }

    @GetMapping("/post/{postId}")
    public List<CommentDto> getCommentsByPost(@PathVariable String postId) {
        return listCommentsByPostUseCase.execute(postId)
                .stream().map(CommentDto::fromComment).toList();
    }

    @GetMapping("/{commentId}")
    public CommentDto findComment(@PathVariable String commentId) {
        return CommentDto.fromComment(findCommentUseCase.execute(commentId));
    }

    @PostMapping("/{commentId}/like")
    public Map<String, Boolean> likeComment(@PathVariable String commentId) {
        var loggedUser = this.getLoggedUser();
        var liked = likeCommentUseCase.execute(commentId, loggedUser);
        return Map.of("liked", liked);
    }

    @PostMapping("/{commentId}/dislike")
    public Map<String, Boolean> dislikeComment(@PathVariable String commentId) {
        var loggedUser = this.getLoggedUser();
        var disliked = dislikeCommentUseCase.execute(commentId, loggedUser);
        return Map.of("disliked", disliked);
    }

}

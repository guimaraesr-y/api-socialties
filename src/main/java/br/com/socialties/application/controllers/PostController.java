package br.com.socialties.application.controllers;

import br.com.socialties.application.usecases.post.*;
import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
import br.com.socialties.domain.post.dtos.CreatePostResponseDto;
import br.com.socialties.domain.post.dtos.PostDto;
import br.com.socialties.domain.post.dtos.UpdatePostRequestDto;
import br.com.socialties.helpers.controllers.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController extends BaseController {

    private final CreatePostUseCase createPostUseCase;
    private final ListPostsUseCase listPostsUseCase;
    private final FindPostUseCase findPostUseCase;
    private final ListPostsByUserUseCase listPostsByUserUseCase;
    private final UpdatePostUseCase updatePostUseCase;
    private final DeletePostUseCase deletePostUseCase;
    private final LikePostUseCase likePostUseCase;
    private final DislikePostUseCase dislikePostUseCase;

    @PostMapping
    public CreatePostResponseDto createPost(@Valid @ModelAttribute CreatePostRequestDto createPostRequestDto) {
        var loggedUser = this.getLoggedUser();
        var post = createPostUseCase.execute(createPostRequestDto, loggedUser);

        return CreatePostResponseDto.fromPost(post);
    }

    @GetMapping
    public List<PostDto> getPosts() {
        // TODO: Implement is follower check
        // TODO: Implement pagination
        // TODO: Add fields liked and disliked
        return listPostsUseCase.execute()
                .stream().map(PostDto::fromPost).toList();
    }

    @GetMapping("/{postId}")
    public PostDto getPost(@PathVariable String postId) {
        return PostDto.fromPost(findPostUseCase.execute(postId));
    }

    @GetMapping("/user/{userId}")
    public List<PostDto> getPostsByUser(@PathVariable String userId) {
        return listPostsByUserUseCase.execute(userId)
                .stream().map(PostDto::fromPost).toList();
    }

    @PutMapping("/{postId}")
    public PostDto updatePost(@PathVariable String postId, @ModelAttribute UpdatePostRequestDto createPostRequestDto) {
        return PostDto.fromPost(updatePostUseCase.execute(postId, createPostRequestDto));
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable String postId) {
        deletePostUseCase.execute(findPostUseCase.execute(postId));
    }

    @PostMapping("/{postId}/like")
    public Map<String, Boolean> likePost(@PathVariable String postId) {
        var loggedUser = this.getLoggedUser();
        var liked = likePostUseCase.execute(postId, loggedUser);

        return Map.of("liked", liked);
    }

    @PostMapping("/{postId}/dislike")
    public Map<String, Boolean> dislikePost(@PathVariable String postId) {
        var loggedUser = this.getLoggedUser();
        var disliked = dislikePostUseCase.execute(postId, loggedUser);

        return Map.of("disliked", disliked);
    }

}

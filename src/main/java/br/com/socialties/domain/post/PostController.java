package br.com.socialties.domain.post;

import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
import br.com.socialties.domain.post.dtos.CreatePostResponseDto;
import br.com.socialties.domain.post.dtos.PostDto;
import br.com.socialties.domain.post.dtos.UpdatePostRequestDto;
import br.com.socialties.helpers.controllers.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController extends BaseController {

    private final PostService postService;

    @PostMapping
    public CreatePostResponseDto createPost(@Valid @ModelAttribute CreatePostRequestDto createPostRequestDto) {
        var loggedUser = this.getLoggedUser();
        var post = postService.createNewPost(createPostRequestDto, loggedUser);

        return new CreatePostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getContentPaths()
        );
    }

    @GetMapping
    public List<PostDto> getPosts() {
        // TODO: Implement is follower check
        // TODO: Implement pagination
        // TODO: Add fields liked and disliked
        return postService.getPosts()
                .stream().map(PostDto::fromPost).toList();
    }

    @GetMapping("/{postId}")
    public PostDto getPost(@PathVariable String postId) {
        return PostDto.fromPost(postService.findPost(postId));
    }

    @GetMapping("/user/{userId}")
    public List<PostDto> getPostsByUser(@PathVariable String userId) {
        return postService.getPostsByUser(userId)
                .stream().map(PostDto::fromPost).toList();
    }

    @PutMapping("/{postId}")
    public PostDto updatePost(@PathVariable String postId, @ModelAttribute UpdatePostRequestDto createPostRequestDto) {
        return PostDto.fromPost(postService.updatePost(postId, createPostRequestDto));
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable String postId) {
        postService.deletePost(postService.findPost(postId));
    }

    @PostMapping("/{postId}/like")
    public Map<String, Boolean> likePost(@PathVariable String postId) {
        var loggedUser = this.getLoggedUser();
        var liked = postService.likePost(postId, loggedUser);

        return Map.of("liked", liked);
    }

    @PostMapping("/{postId}/dislike")
    public Map<String, Boolean> dislikePost(@PathVariable String postId) {
        var loggedUser = this.getLoggedUser();
        var disliked = postService.dislikePost(postId, loggedUser);

        return Map.of("disliked", disliked);
    }

}

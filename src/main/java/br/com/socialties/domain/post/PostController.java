package br.com.socialties.domain.post;

import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
import br.com.socialties.domain.post.dtos.CreatePostResponseDto;
import br.com.socialties.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public CreatePostResponseDto createPost(@Valid @ModelAttribute CreatePostRequestDto createPostRequestDto, Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        var post = postService.createNewPost(createPostRequestDto, loggedUser);

        return new CreatePostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getContentPaths()
        );
    }

    @PostMapping("/{postId}/like")
    public Map<String, Boolean> likePost(@PathVariable String postId, Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        var liked = postService.likePost(postId, loggedUser);

        return Map.of("liked", liked);
    }

    @PostMapping("/{postId}/dislike")
    public Map<String, Boolean> dislikePost(@PathVariable String postId, Principal principal) {
        var loggedUser = (User) ((Authentication) principal).getPrincipal();
        var disliked = postService.dislikePost(postId, loggedUser);

        return Map.of("disliked", disliked);
    }

}

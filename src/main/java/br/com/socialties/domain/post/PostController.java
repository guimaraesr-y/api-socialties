package br.com.socialties.domain.post;

import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
import br.com.socialties.domain.post.dtos.CreatePostResponseDto;
import br.com.socialties.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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

}

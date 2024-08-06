package br.com.socialties.domain.post;

import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
import br.com.socialties.domain.post.dtos.CreatePostResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public CreatePostResponseDto createPost(@Valid @ModelAttribute CreatePostRequestDto createPostRequestDto) {
        var post = postService.createNewPost(createPostRequestDto);
        return new CreatePostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getContentPaths()
        );
    }

}

package br.com.socialties.domain.post;

import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
import br.com.socialties.domain.post.exceptions.PostNotFoundException;
import br.com.socialties.domain.storage.StorageService;
import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final StorageService storageService;
    private final UserService userService;

    public Post createNewPost(CreatePostRequestDto createPostRequestDto, User loggedUser) {
        var user = userService.findUser(loggedUser);
        var post = new Post();

        post.setTitle(createPostRequestDto.title());
        post.setDescription(createPostRequestDto.description());
        post.setContentPaths(new ArrayList<>());

        post.setAuthor(user);
        post.setLikes(new ArrayList<>());
        post.setDislikes(new ArrayList<>());

        for (MultipartFile file : createPostRequestDto.contents()) {
            post.getContentPaths().add(storageService.store(file));
        }

        return postRepository.save(post);
    }

    public Post findPost(String postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }
}

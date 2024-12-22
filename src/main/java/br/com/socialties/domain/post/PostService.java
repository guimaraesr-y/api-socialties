package br.com.socialties.domain.post;

import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
import br.com.socialties.domain.post.dtos.UpdatePostRequestDto;
import br.com.socialties.domain.post.exceptions.PostNotFoundException;
import br.com.socialties.domain.storage.StorageService;
import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final StorageService storageService;
    private final UserService userService;

    public Post createNewPost(CreatePostRequestDto createPostRequestDto, User loggedUser) {
        var user = userService.findUser(loggedUser);
        var post = new Post();

        post.setTitle(createPostRequestDto.title());
        post.setDescription(createPostRequestDto.description());
        post.setAuthor(user);

        // store the files
        var files = createPostRequestDto.contents();
        if(files != null && files.isPresent()) {
            for (MultipartFile file : files.get()) {
                post.getContentPaths().add(storageService.store(file));
            }
        }

        return postRepository.save(post);
    }

    public List<Post> getPosts() {
        // TODO: implement pagination
        return postRepository.findAll();
    }

    public List<Post> getPostsByUser(String userId) {
        var user = userService.findUser(userId);
        return postRepository.findAllByAuthor(user);
    }

    public Boolean likePost(String postId, User loggedUser) {
        var post = findPost(postId);
        var user = userService.findUser(loggedUser);

        var liked = post.like(user);
        postRepository.save(post);

        return liked;
    }

    public Boolean dislikePost(String postId, User loggedUser) {
        var post = findPost(postId);
        var user = userService.findUser(loggedUser);

        var disliked = post.dislike(user);
        postRepository.save(post);
        
        return disliked;
    }

    public Post findPost(String postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    public Post updatePost(String postId, UpdatePostRequestDto createPostRequestDto) {
        var post = findPost(postId);

        if(createPostRequestDto.title().isPresent()) {
            post.setTitle(createPostRequestDto.title().get());
        }

        if(createPostRequestDto.description().isPresent()) {
            post.setDescription(createPostRequestDto.description().get());
        }

        if(createPostRequestDto.contents().isPresent()) {
            post.getContentPaths().forEach(storageService::delete);
            post.getContentPaths().clear();

            for (MultipartFile file : createPostRequestDto.contents().get()) {
                post.getContentPaths().add(storageService.store(file));
            }
        }

        return postRepository.save(post);
    }

    public void deletePost(Post post) {
        postRepository.delete(post);
    }

}

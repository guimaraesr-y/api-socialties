package br.com.socialties.domain.post;

import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
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
        post.setContentPaths(new ArrayList<>());

        post.setAuthor(user);
        post.setLikes(new ArrayList<>());
        post.setDislikes(new ArrayList<>());
        post.setComments(new ArrayList<>());
        post.setLikesCount(0);
        post.setDislikesCount(0);
        post.setCommentsCount(0);

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

        // finds the full user object from the logged user
        var user = userService.findUser(loggedUser);

        // check if the user already liked the post and remove the like
        if(post.getLikes().contains(user)) {
            post.getLikes().remove(loggedUser);
            post.setLikesCount(post.getLikesCount() - 1);
            return false;
        }

        post.getLikes().add(loggedUser);
        post.setLikesCount(post.getLikesCount() + 1);

        postRepository.save(post);
        return true;
    }

    public Boolean dislikePost(String postId, User loggedUser) {
        var post = findPost(postId);

        // finds the full user object from the logged user
        var user = userService.findUser(loggedUser);

        // check if the user already disliked the post and remove the dislike
        if(post.getDislikes().contains(user)) {
            post.getDislikes().remove(loggedUser);
            post.setDislikesCount(post.getDislikesCount() - 1);
            return false;
        }

        post.getDislikes().add(loggedUser);
        post.setDislikesCount(post.getDislikesCount() + 1);

        postRepository.save(post);
        return true;
    }

    public Post findPost(String postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    public void deletePost(Post post) {
        postRepository.delete(post);
    }

}

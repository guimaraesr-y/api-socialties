package br.com.socialties.post;

import br.com.socialties.domain.authentication.AuthService;
import br.com.socialties.domain.authentication.dtos.RegisterRequestDto;
import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.post.PostService;
import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
import br.com.socialties.domain.user.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;
    
    private User john;
    private User jane;
    private Post firstPost;

    public void setup() {
        try {
            john = authService.register(
                    new RegisterRequestDto("John Doe", "johndoe@exameple.com", "password")
            );
            jane = authService.register(
                    new RegisterRequestDto("Jane Doe", "janedoe@exameple.com", "password")
            );
            firstPost = postService.createNewPost(
                    new CreatePostRequestDto("Hello World!", "My first post", null),
                    john
            );

            if (john == null || jane == null || firstPost == null) {
                throw new RuntimeException("Failed to initialize test data.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Setup failed: " + e.getMessage());
        }
    }

    public void cleanup() {
        try {
            postService.deletePost(firstPost);
            authService.deleteUser(john);
            authService.deleteUser(jane);

            john = null;
            jane = null;
            firstPost = null;
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Cleanup failed: " + e.getMessage());
        }
    }

    @BeforeEach
    public void beforeEach() {
        setup();
    }

    @AfterEach
    public void afterEach() {
        cleanup();
    }

    @Test
    @Order(1)
    public void createPost() {
        var createdPost = postService.createNewPost(
                new CreatePostRequestDto("Hello World!", "My second post", null),
                john
        );

        Assertions.assertNotNull(createdPost);
    }

    @Test
    @Order(2)
    public void likePost() {
        System.out.println(firstPost);
        var liked = postService.likePost(firstPost.getId(), jane);
        Assertions.assertTrue(liked);
    }
    
    @Test
    @Order(3)
    public void unlikePost() {
        var likeFirst = postService.likePost(firstPost.getId(), jane);
        var unlike = postService.likePost(firstPost.getId(), jane);
        Assertions.assertFalse(unlike);
    }
    
    @Test
    @Order(4)
    public void dislikePost() {
        var disliked = postService.dislikePost(firstPost.getId(), jane);
        Assertions.assertTrue(disliked);
    }
    
    @Test
    @Order(5)
    public void undislikePost() {
        postService.dislikePost(firstPost.getId(), jane);
        var disliked = postService.dislikePost(firstPost.getId(), jane);
        Assertions.assertFalse(disliked);
    }
    
}

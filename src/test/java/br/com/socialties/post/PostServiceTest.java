package br.com.socialties.post;

import br.com.socialties.domain.authentication.AuthService;
import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.post.PostService;
import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
import br.com.socialties.domain.user.User;
import br.com.socialties.post.helpers.PostTestHelper;
import br.com.socialties.user.helpers.UserTestHelper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserTestHelper userTestHelper;

    @Autowired
    private PostTestHelper postTestHelper;
    
    private User john;
    private User jane;
    private Post firstPost;

    public void setup() {
        john = userTestHelper.createUser();

        var janeUser = new User();
        janeUser.setName("Jane Doe");
        janeUser.setEmail("janedoe@exameple.com");
        jane = userTestHelper.createUser(janeUser);

        firstPost = postTestHelper.createPost(john, new Post());
    }

    public void cleanup() {
        postTestHelper.tearDown();
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
    public void createPost() {
        var createdPost = postService.createNewPost(
                new CreatePostRequestDto("Hello World!", "My second post", null),
                john
        );

        Assertions.assertNotNull(createdPost);
    }

    @Test
    public void likePost() {
        System.out.println(firstPost);
        var liked = postService.likePost(firstPost.getId(), jane);
        Assertions.assertTrue(liked);
    }
    
    @Test
    public void unlikePost() {
        var likeFirst = postService.likePost(firstPost.getId(), jane);
        var unlike = postService.likePost(firstPost.getId(), jane);
        Assertions.assertFalse(unlike);
    }
    
    @Test
    public void dislikePost() {
        var disliked = postService.dislikePost(firstPost.getId(), jane);
        Assertions.assertTrue(disliked);
    }
    
    @Test
    public void undislikePost() {
        postService.dislikePost(firstPost.getId(), jane);
        var disliked = postService.dislikePost(firstPost.getId(), jane);
        Assertions.assertFalse(disliked);
    }
    
}

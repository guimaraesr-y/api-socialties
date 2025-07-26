package br.com.socialties.post;

import br.com.socialties.application.usecases.post.CreatePostUseCase;
import br.com.socialties.application.usecases.post.DislikePostUseCase;
import br.com.socialties.application.usecases.post.LikePostUseCase;
import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.post.PostRepository;
import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
import br.com.socialties.domain.user.User;
import br.com.socialties.post.helpers.PostTestHelper;
import br.com.socialties.user.helpers.UserTestHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
@Transactional
public class PostUseCaseTest {

    @Autowired
    private UserTestHelper userTestHelper;

    @Autowired
    private PostTestHelper postTestHelper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CreatePostUseCase createPostUseCase;

    @Autowired
    private LikePostUseCase likePostUseCase;

    @Autowired
    private DislikePostUseCase dislikePostUseCase;

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
        var createdPost = createPostUseCase.execute(
                new CreatePostRequestDto("Hello World!", "My second post", null),
                john
        );

        Assertions.assertNotNull(createdPost);
    }

    @Test
    public void likePost() {
        var liked = likePostUseCase.execute(firstPost.getId(), jane);

        Assertions.assertTrue(liked);
        Assertions.assertTrue(firstPost.getLikes().contains(jane));
    }

    @Test
    public void unlikePost() {
        var likeFirst = likePostUseCase.execute(firstPost.getId(), jane);
        var unlike = likePostUseCase.execute(firstPost.getId(), jane);
        Assertions.assertFalse(unlike);

    }

    @Test
    public void dislikePost() {
        var disliked = dislikePostUseCase.execute(firstPost.getId(), jane);
        Assertions.assertTrue(disliked);
        Assertions.assertTrue(firstPost.getDislikes().contains(jane));
    }

    @Test
    public void undislikePost() {
        dislikePostUseCase.execute(firstPost.getId(), jane);
        var disliked = dislikePostUseCase.execute(firstPost.getId(), jane);
        Assertions.assertFalse(disliked);
        Assertions.assertFalse(firstPost.getDislikes().contains(jane));
    }

    @Test
    public void dislikePostWhenLiked() {
        var liked = likePostUseCase.execute(firstPost.getId(), jane);
        Assertions.assertTrue(liked);

        var disliked = dislikePostUseCase.execute(firstPost.getId(), jane);
        Assertions.assertFalse(firstPost.getLikes().contains(jane));
        Assertions.assertTrue(disliked);
    }

    @Test
    public void likePostWhenDisliked() {
        var disliked = dislikePostUseCase.execute(firstPost.getId(), jane);
        Assertions.assertTrue(disliked);

        var liked = likePostUseCase.execute(firstPost.getId(), jane);
        Assertions.assertFalse(firstPost.getDislikes().contains(jane));
        Assertions.assertTrue(liked);
    }

}

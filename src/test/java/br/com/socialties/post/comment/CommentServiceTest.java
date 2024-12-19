package br.com.socialties.post.comment;

import br.com.socialties.domain.authentication.AuthService;
import br.com.socialties.domain.authentication.dtos.RegisterRequestDto;
import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.post.PostService;
import br.com.socialties.domain.post.comment.Comment;
import br.com.socialties.domain.post.comment.CommentService;
import br.com.socialties.domain.post.comment.dtos.CreateCommentRequestDto;
import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
import br.com.socialties.domain.user.User;
import br.com.socialties.post.comment.helpers.CommentTestHelper;
import br.com.socialties.post.helpers.PostTestHelper;
import br.com.socialties.user.helpers.UserTestHelper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class CommentServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CommentTestHelper commentTestHelper;

    @Autowired
    private PostTestHelper postTestHelper;

    @Autowired
    private UserTestHelper userTestHelper;

    private User john;
    private Post firstPost;
    private Comment firstComment;

    public void setup() {
        john = userTestHelper.createUser();
        firstPost = postTestHelper.createPost(john);
        firstComment = commentTestHelper.createComment(firstPost, john);
    }

    public void cleanup() {
        userTestHelper.tearDown();
        postTestHelper.tearDown();
        commentTestHelper.tearDown();
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
    public void createComment() {
        var comment = commentService.createComment(
                firstPost.getId(),
                new CreateCommentRequestDto("My first comment"),
                john);

        Assertions.assertNotNull(comment);
    }

    @Test
    @Order(2)
    public void likeComment() {
        var comment = commentService.createComment(
                firstPost.getId(),
                new CreateCommentRequestDto("My first comment"),
                john
        );
        var like = commentService.likeComment(comment.getId(), john);

        Assertions.assertTrue(like);
    }

    @Test
    @Order(3)
    public void unlikeComment() {
        var comment = commentService.createComment(
                firstPost.getId(),
                new CreateCommentRequestDto("My first comment"),
                john
        );

        var like = commentService.likeComment(comment.getId(), john);
        var unlike = commentService.likeComment(comment.getId(), john);
        Assertions.assertFalse(unlike);
    }

    @Test
    @Order(4)
    public void dislikeComment() {
        var comment = commentService.createComment(
                firstPost.getId(),
                new CreateCommentRequestDto("My first comment"),
                john
        );

        var dislike = commentService.dislikeComment(comment.getId(), john);
        Assertions.assertTrue(dislike);
    }

    @Test
    @Order(5)
    public void undislikeComment() {
        var comment = commentService.createComment(
                firstPost.getId(),
                new CreateCommentRequestDto("My first comment"),
                john
        );

        var dislike = commentService.dislikeComment(comment.getId(), john);
        var undislike = commentService.dislikeComment(comment.getId(), john);
        Assertions.assertFalse(undislike);
    }

}

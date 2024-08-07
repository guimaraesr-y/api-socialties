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
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class CommentServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthService authService;

    private User john;
    private User jane;
    private Post firstPost;

    public void setup() {
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
    }

    public void cleanup() {
        postService.deletePost(firstPost);
        authService.deleteUser(john);
        authService.deleteUser(jane);
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
                jane);

        Assertions.assertNotNull(comment);
    }

    @Test
    @Order(2)
    public void likeComment() {
        var comment = commentService.createComment(
                firstPost.getId(),
                new CreateCommentRequestDto("My first comment"),
                jane
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
                jane
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
                jane
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
                jane
        );

        var dislike = commentService.dislikeComment(comment.getId(), john);
        var undislike = commentService.dislikeComment(comment.getId(), john);
        Assertions.assertFalse(undislike);
    }

}

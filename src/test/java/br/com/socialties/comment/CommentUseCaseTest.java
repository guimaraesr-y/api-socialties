package br.com.socialties.comment;

import br.com.socialties.application.usecases.comment.CreateCommentUseCase;
import br.com.socialties.application.usecases.comment.DislikeCommentUseCase;
import br.com.socialties.application.usecases.comment.LikeCommentUseCase;
import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.comment.Comment;
import br.com.socialties.domain.comment.dtos.CreateCommentRequestDto;
import br.com.socialties.domain.user.User;
import br.com.socialties.comment.helpers.CommentTestHelper;
import br.com.socialties.post.helpers.PostTestHelper;
import br.com.socialties.user.helpers.UserTestHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class CommentUseCaseTest {

    @Autowired
    private CreateCommentUseCase createComment;

    @Autowired
    private LikeCommentUseCase likeComment;

    @Autowired
    private DislikeCommentUseCase dislikeComment;

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
        commentTestHelper.tearDown();
        postTestHelper.tearDown();
        userTestHelper.tearDown();
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
        var comment = createComment.execute(
                firstPost.getId(),
                new CreateCommentRequestDto("My first comment"),
                john
        );
        Assertions.assertNotNull(comment);
    }

    @Test
    @Order(2)
    public void likeComment() {
        var comment = createComment.execute(
                firstPost.getId(),
                new CreateCommentRequestDto("My first comment"),
                john
        );
        var like = likeComment.execute(comment.getId(), john);
        Assertions.assertTrue(like);
    }

    @Test
    @Order(3)
    public void unlikeComment() {
        var comment = createComment.execute(
                firstPost.getId(),
                new CreateCommentRequestDto("My first comment"),
                john
        );

        var like = likeComment.execute(comment.getId(), john);
        var unlike = likeComment.execute(comment.getId(), john);
        Assertions.assertFalse(unlike);
    }

    @Test
    @Order(4)
    public void dislikeComment() {
        var comment = createComment.execute(
                firstPost.getId(),
                new CreateCommentRequestDto("My first comment"),
                john
        );

        var dislike = dislikeComment.execute(comment.getId(), john);
        Assertions.assertTrue(dislike);
    }

    @Test
    @Order(5)
    public void undislikeComment() {
        var comment = createComment.execute(
                firstPost.getId(),
                new CreateCommentRequestDto("My first comment"),
                john
        );

        var dislike = dislikeComment.execute(comment.getId(), john);
        var undislike = dislikeComment.execute(comment.getId(), john);
        Assertions.assertFalse(undislike);
    }

}

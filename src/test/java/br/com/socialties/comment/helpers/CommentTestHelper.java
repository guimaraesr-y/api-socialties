package br.com.socialties.comment.helpers;

import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.comment.Comment;
import br.com.socialties.domain.comment.CommentRepository;
import br.com.socialties.domain.user.User;
import br.com.socialties.helpers.controllers.utils.ModelMapperUtil;
import br.com.socialties.post.helpers.PostTestHelper;
import br.com.socialties.user.helpers.UserTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentTestHelper {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostTestHelper postTestHelper;

    @Autowired
    private UserTestHelper userTestHelper;

    public Comment createComment(Post post, User author, Comment comment) {
        if (comment.getId() != null) {
            return comment;
        }

        var savedPost = postTestHelper.createPost(post.getAuthor(), post);
        var savedAuthor = userTestHelper.createUser(author);

        var defaultComment = new Comment();
        defaultComment.setPost(savedPost);
        defaultComment.setText("My first comment");
        defaultComment.setAuthor(savedAuthor);

        ModelMapperUtil.mapNonNullProperties(comment, defaultComment);

        var newComment = commentRepository.save(defaultComment);
        return commentRepository.save(newComment);
    }

    public Comment createComment(Post post, User author) {
        return createComment(post, author, new Comment());
    }

    public Comment createComment(Post post) {
        return createComment(post, new User(), new Comment());
    }

    public void tearDown() {
        commentRepository.deleteAll();
    }

}

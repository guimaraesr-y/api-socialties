package br.com.socialties.post.helpers;

import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.post.PostRepository;
import br.com.socialties.domain.user.User;
import br.com.socialties.helpers.controllers.utils.ModelMapperUtil;
import br.com.socialties.user.helpers.UserTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostTestHelper {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserTestHelper userTestHelper;

    public Post createPost(User user, Post post) {
        var owner = userTestHelper.createUser(user);

        var defaultPost = new Post();
        defaultPost.setAuthor(owner);
        defaultPost.setTitle("Hello World!");
        defaultPost.setDescription("My first post");

        ModelMapperUtil.mapNonNullProperties(post, defaultPost);

        var newPost = postRepository.save(defaultPost);
        return postRepository.save(newPost);
    }

    public Post createPost() {
        return createPost(new User(), new Post());
    }

    public void tearDown() {
        postRepository.deleteAll();
        userTestHelper.tearDown();
    }

}

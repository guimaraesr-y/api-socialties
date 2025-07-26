package br.com.socialties.application.usecases.post;

import br.com.socialties.application.usecases.user.FindUserUseCase;
import br.com.socialties.domain.post.PostRepository;
import br.com.socialties.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DislikePostUseCase {

    private final PostRepository postRepository;
    private final FindPostUseCase findPostUseCase;
    private final FindUserUseCase findUserUseCase;

    public Boolean execute(String postId, User loggedUser) {
        var post = findPostUseCase.execute(postId);
        var user = findUserUseCase.execute(loggedUser);

        var disliked = post.dislike(user);
        postRepository.save(post);

        return disliked;
    }
}

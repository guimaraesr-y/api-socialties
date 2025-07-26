package br.com.socialties.application.usecases.post;

import br.com.socialties.application.usecases.user.FindUserUseCase;
import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListPostsByUserUseCase {

    private final PostRepository postRepository;
    private final FindUserUseCase findUserUseCase;

    public List<Post> execute(String userId) {
        var user = findUserUseCase.execute(userId);
        return postRepository.findAllByAuthor(user);
    }
}

package br.com.socialties.application.usecases.post;

import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletePostUseCase {

    private final PostRepository postRepository;

    public void execute(Post post) {
        postRepository.delete(post);
    }

}

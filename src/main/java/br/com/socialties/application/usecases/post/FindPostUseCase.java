package br.com.socialties.application.usecases.post;

import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.post.PostRepository;
import br.com.socialties.domain.post.exceptions.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindPostUseCase {

    private final PostRepository postRepository;

    public Post execute(String postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }
}

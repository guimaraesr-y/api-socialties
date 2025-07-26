package br.com.socialties.application.usecases.post;

import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListPostsUseCase {

    private final PostRepository postRepository;

    public List<Post> execute() {
        return postRepository.findAll();
    }
}

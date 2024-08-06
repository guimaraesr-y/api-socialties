package br.com.socialties.domain.post;

import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
import br.com.socialties.domain.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final StorageService storageService;

    public Post createNewPost(CreatePostRequestDto createPostRequestDto) {
        var post = new Post();
        post.setTitle(createPostRequestDto.title());
        post.setDescription(createPostRequestDto.description());
        post.setContentPaths(new ArrayList<>());

        for (MultipartFile file : createPostRequestDto.contents()) {
            post.getContentPaths().add(storageService.store(file));
        }

        return postRepository.save(post);
    }

}

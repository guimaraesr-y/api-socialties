package br.com.socialties.application.usecases.post;

import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.post.PostRepository;
import br.com.socialties.domain.post.dtos.UpdatePostRequestDto;
import br.com.socialties.domain.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UpdatePostUseCase {

    private final PostRepository postRepository;
    private final StorageService storageService;
    private final FindPostUseCase findPostUseCase;

    public Post execute(String postId, UpdatePostRequestDto createPostRequestDto) {
        var post = findPostUseCase.execute(postId);

        if(createPostRequestDto.title().isPresent()) {
            post.setTitle(createPostRequestDto.title().get());
        }

        if(createPostRequestDto.description().isPresent()) {
            post.setDescription(createPostRequestDto.description().get());
        }

        if(createPostRequestDto.contents().isPresent()) {
            post.getContentPaths().forEach(storageService::delete);
            post.getContentPaths().clear();

            for (MultipartFile file : createPostRequestDto.contents().get()) {
                post.getContentPaths().add(storageService.store(file));
            }
        }

        return postRepository.save(post);
    }
}

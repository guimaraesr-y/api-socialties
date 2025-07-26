package br.com.socialties.application.usecases.post;

import br.com.socialties.application.usecases.user.FindUserUseCase;
import br.com.socialties.domain.post.Post;
import br.com.socialties.domain.post.PostRepository;
import br.com.socialties.domain.post.dtos.CreatePostRequestDto;
import br.com.socialties.domain.storage.StorageService;
import br.com.socialties.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CreatePostUseCase {

    private final PostRepository postRepository;
    private final StorageService storageService;
    private final FindUserUseCase findUserUseCase;

    public Post execute(CreatePostRequestDto createPostRequestDto, User loggedUser) {
        var user = findUserUseCase.execute(loggedUser);
        var post = new Post();

        post.setTitle(createPostRequestDto.title());
        post.setDescription(createPostRequestDto.description());
        post.setAuthor(user);

        var files = createPostRequestDto.contents();
        if(files != null && files.isPresent()) {
            for (MultipartFile file : files.get()) {
                post.getContentPaths().add(storageService.store(file));
            }
        }

        return postRepository.save(post);
    }

}

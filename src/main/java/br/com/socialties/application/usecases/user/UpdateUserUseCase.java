package br.com.socialties.application.usecases.user;

import br.com.socialties.domain.storage.StorageService;
import br.com.socialties.domain.user.User;
import br.com.socialties.domain.user.UserRepository;
import br.com.socialties.domain.user.dtos.UpdateUserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;
    private final FindUserUseCase findUserUseCase;

    public User execute(User loggedUser, UpdateUserRequestDto updateUserRequestDto) {
        var userEdit = findUserUseCase.execute(loggedUser);

        if(updateUserRequestDto.name().isPresent()) {
            userEdit.setName(updateUserRequestDto.name().get());
        }

        if(updateUserRequestDto.password().isPresent()) {
            var password = updateUserRequestDto.password().get();
            userEdit.setPassword(passwordEncoder.encode(passwordEncoder.encode(password)));
        }

        if(updateUserRequestDto.profilePicture().isPresent()) {
            storageService.delete(userEdit.getProfilePicturePath());
            var profilePicturePath = storageService
                    .store(updateUserRequestDto.profilePicture().get());
            userEdit.setProfilePicturePath(profilePicturePath);
        }

        return userRepository.save(userEdit);
    }
}

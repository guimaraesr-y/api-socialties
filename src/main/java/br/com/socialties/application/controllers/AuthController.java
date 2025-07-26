package br.com.socialties.application.controllers;

import br.com.socialties.application.usecases.auth.LoginUserUseCase;
import br.com.socialties.application.usecases.auth.RegisterUserUseCase;
import br.com.socialties.domain.authentication.dtos.LoginRequestDto;
import br.com.socialties.domain.authentication.dtos.LoginResponseDto;
import br.com.socialties.domain.authentication.dtos.RegisterRequestDto;
import br.com.socialties.domain.authentication.dtos.RegisterResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUserUseCase loginUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        var token = loginUserUseCase.execute(loginRequestDto);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/register")
    public RegisterResponseDto register(@Valid @ModelAttribute RegisterRequestDto registerRequestDto) {
        var user = registerUserUseCase.execute(registerRequestDto);
        return RegisterResponseDto.fromUser(user);
    }

}

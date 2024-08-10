package br.com.socialties.domain.authentication;

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

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        var token = service.login(loginRequestDto);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/register")
    public RegisterResponseDto register(@Valid @ModelAttribute RegisterRequestDto registerRequestDto) {
        var user = service.register(registerRequestDto);
        return RegisterResponseDto.fromUser(user);
    }

}

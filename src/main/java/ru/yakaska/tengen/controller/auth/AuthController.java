package ru.yakaska.tengen.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yakaska.tengen.controller.auth.dto.JwtAuthResponse;
import ru.yakaska.tengen.controller.auth.dto.LoginDto;
import ru.yakaska.tengen.controller.auth.dto.RegisterDto;
import ru.yakaska.tengen.controller.auth.dto.RegisterResponse;
import ru.yakaska.tengen.service.AuthService;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterDto registerDto) {
        RegisterResponse response = new RegisterResponse(authService.register(registerDto));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}

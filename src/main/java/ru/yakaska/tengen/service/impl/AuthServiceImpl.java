package ru.yakaska.tengen.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yakaska.tengen.controller.auth.dto.LoginDto;
import ru.yakaska.tengen.controller.auth.dto.RegisterDto;
import ru.yakaska.tengen.entity.User;
import ru.yakaska.tengen.exception.UserAlreadyExistsException;
import ru.yakaska.tengen.repository.UserRepository;
import ru.yakaska.tengen.security.JwtTokenProvider;
import ru.yakaska.tengen.service.AuthService;

@Service
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new UserAlreadyExistsException("User already exists.");
        }

        User user = User.builder()
                .username(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .build();
        userRepository.save(user);
        return "User registered successfully.";
    }
}

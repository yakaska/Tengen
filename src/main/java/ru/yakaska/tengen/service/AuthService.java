package ru.yakaska.tengen.service;


import ru.yakaska.tengen.controller.auth.dto.LoginDto;
import ru.yakaska.tengen.controller.auth.dto.RegisterDto;

public interface AuthService {

    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);

}

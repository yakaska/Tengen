package ru.yakaska.tengen.service;


import ru.yakaska.tengen.controller.auth.dto.LoginDto;
import ru.yakaska.tengen.controller.auth.dto.RegisterDto;
import ru.yakaska.tengen.entity.User;

public interface AuthService {

    String login(LoginDto loginDto);

    User register(RegisterDto registerDto);

}

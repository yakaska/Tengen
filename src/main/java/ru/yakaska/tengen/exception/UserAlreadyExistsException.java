package ru.yakaska.tengen.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyExistsException extends ResponseStatusException {

    public UserAlreadyExistsException(String username) {
        super(HttpStatus.CONFLICT, String.format("User with such username is already exists: %s", username));
    }
}
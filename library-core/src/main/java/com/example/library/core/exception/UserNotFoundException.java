package com.example.library.core.exception;

public class UserNotFoundException extends Exception {

    private UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException createWithId(Long id) {
        String msg = String.format("User with id: %s doesn't exist!", id);
        return new UserNotFoundException(msg);
    }

    public static UserNotFoundException createWithUsername(String username) {
        String msg = String.format("User with username: %s doesn't exist!", username);
        return new UserNotFoundException(msg);
    }
}

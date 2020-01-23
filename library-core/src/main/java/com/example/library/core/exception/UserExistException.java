package com.example.library.core.exception;

public class UserExistException extends Exception {

    private UserExistException(String message) {
        super(message);
    }

    public static UserExistException createWithUsername(String username) {
        String msg = String.format("User with username: %s already exist!", username);
        return new UserExistException(msg);
    }
}

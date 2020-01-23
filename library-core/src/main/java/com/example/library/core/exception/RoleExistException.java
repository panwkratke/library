package com.example.library.core.exception;

public class RoleExistException extends Exception {

    private RoleExistException(String message) {
        super(message);
    }

    public static RoleExistException createWithId(Long id) {
        String msg = String.format("Role with id: %s already exist!", id);
        return new RoleExistException(msg);
    }
}

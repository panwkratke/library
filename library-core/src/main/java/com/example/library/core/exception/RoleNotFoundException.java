package com.example.library.core.exception;

public class RoleNotFoundException extends Exception {

    private RoleNotFoundException(String message) {
        super(message);
    }

    public static RoleNotFoundException createWithId(Long id) {
        String msg = String.format("Role with id: %s doesn't exist!", id);
        return new RoleNotFoundException(msg);
    }

    public static RoleNotFoundException createWithRoleName(String roleName) {
        String msg = String.format("Role with name: %s doesn't exist!", roleName);
        return new RoleNotFoundException(msg);
    }
}

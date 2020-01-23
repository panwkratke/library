package com.example.library.core.exception;

public class BorrowOrderNotFoundException extends Exception {

    private BorrowOrderNotFoundException(String message) {
        super(message);
    }

    public static BorrowOrderNotFoundException createWithId(Long id) {
        String msg = String.format("BorrowOrder with id: %s doesn't exist!", id);
        return new BorrowOrderNotFoundException(msg);
    }
}

package com.example.library.core.exception;

public class ActiveBorrowOrdersException extends Exception {

    private ActiveBorrowOrdersException(String message) {
        super(message);
    }

    public static ActiveBorrowOrdersException createWithUserId(Long id) {
        String msg = String.format("User with id: %s has active borrow orders!", id);
        return new ActiveBorrowOrdersException(msg);
    }

    public static ActiveBorrowOrdersException createWithBookId(Long id) {
        String msg = String.format("Book with id: %s has active borrow orders!", id);
        return new ActiveBorrowOrdersException(msg);
    }
}

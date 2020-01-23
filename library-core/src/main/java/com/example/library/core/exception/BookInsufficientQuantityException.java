package com.example.library.core.exception;

public class BookInsufficientQuantityException extends Exception {

    private BookInsufficientQuantityException(String message) {
        super(message);
    }

    public static BookInsufficientQuantityException createWithId(Long id) {
        String msg = String.format("Book with id: %s insufficient quantity in stock.", id);
        return new BookInsufficientQuantityException(msg);
    }
}

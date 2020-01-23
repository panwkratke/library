package com.example.library.core.exception;

public class BookExistException extends Exception {

    private BookExistException(String message) {
        super(message);
    }

    public static BookExistException createWithId(Long id) {
        String msg = String.format("Book with id: %s already exist!", id);
        return new BookExistException(msg);
    }
}

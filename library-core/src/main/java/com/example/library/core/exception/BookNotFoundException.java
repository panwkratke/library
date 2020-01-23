package com.example.library.core.exception;

public class BookNotFoundException extends Exception {

    private BookNotFoundException(String message) {
        super(message);
    }

    public static BookNotFoundException createWithId(Long id) {
        String msg = String.format("Book with id: %s doesn't exist!", id);
        return new BookNotFoundException(msg);
    }

    public static BookNotFoundException createWithTitleAndAuthor(String title, String author) {
        String msg = String.format("Book with title: %s by author: %s doesn't exist!", title, author);
        return new BookNotFoundException(msg);
    }
}

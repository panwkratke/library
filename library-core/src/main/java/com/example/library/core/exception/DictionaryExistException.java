package com.example.library.core.exception;

public class DictionaryExistException extends Exception {

    private DictionaryExistException(String message) {
        super(message);
    }

    public static DictionaryExistException createWithId(Long id) {
        String msg = String.format("Dictionary with id: %s already exist!", id);
        return new DictionaryExistException(msg);
    }
}

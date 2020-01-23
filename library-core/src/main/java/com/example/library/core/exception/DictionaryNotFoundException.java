package com.example.library.core.exception;

public class DictionaryNotFoundException extends Exception {

    private DictionaryNotFoundException(String message) {
        super(message);
    }

    public static DictionaryNotFoundException createWithId(Long id) {
        String msg = String.format("Dictionary with id: %s doesn't exist!", id);
        return new DictionaryNotFoundException(msg);
    }

    public static DictionaryNotFoundException createWithDictionaryName(String dictionaryName) {
        String msg = String.format("Dictionary with dictionaryName: %s doesn't exist!", dictionaryName);
        return new DictionaryNotFoundException(msg);
    }
}

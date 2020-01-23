package com.example.library.core.exception;

public class BorrowOrderExistException extends Exception {

    private BorrowOrderExistException(String message) {
        super(message);
    }

    public static BorrowOrderExistException createWithId(Long id) {
        String msg = String.format("Borrow order with id: %s already exist!", id);
        return new BorrowOrderExistException(msg);
    }

    public static BorrowOrderExistException createWithBookIdAndUserId(Long bookId, Long userId) {
        String msg = String.format("BorrowOrder with book id: %s and user id: %s already exist!", bookId, userId);
        return new BorrowOrderExistException(msg);
    }
}

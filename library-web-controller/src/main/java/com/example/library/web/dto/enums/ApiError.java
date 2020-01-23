package com.example.library.web.dto.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiError {

    DUPLICATED_USER(HttpStatus.BAD_REQUEST, -1001, "Duplicated user login"),
    USER_DOES_NOT_EXIST(HttpStatus.BAD_REQUEST, -1002, "User doesn't exist"),
    USER_INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, -1003, "User incorrect password"),
    DUPLICATED_BOOK(HttpStatus.BAD_REQUEST, -1004, "Duplicated book"),
    BOOK_DOES_NOT_EXIST(HttpStatus.BAD_REQUEST, -1005, "Book doesn't exist"),
    BOOK_INSUFFICIENT_QUANTITY(HttpStatus.BAD_REQUEST, -1006, "Book insufficient quantity in stock"),
    DUPLICATED_BORROW_ORDER(HttpStatus.BAD_REQUEST, -1007, "Duplicated borrow order"),
    BORROW_ORDER_DOES_NOT_EXIST(HttpStatus.BAD_REQUEST, -1008, "Borrow order doesn't exist"),
    ROLE_DOES_NOT_EXIST(HttpStatus.BAD_REQUEST, -1009, "Role doesn't exist"),
    ACTIVE_BORROW_ORDERS(HttpStatus.BAD_REQUEST, -1010, "User/Book has active borrow orders"),
    DICTIONARY_DOES_NOT_EXIST(HttpStatus.BAD_REQUEST, -1011, "Dictionary doesn't exist")
    ;

    private final HttpStatus httpStatus;
    private final int apiError;
    private final String apiErrorMsg;

    ApiError(HttpStatus httpStatus, int apiError, String apiErrorMsg) {
        this.httpStatus = httpStatus;
        this.apiError = apiError;
        this.apiErrorMsg = apiErrorMsg;
    }
}

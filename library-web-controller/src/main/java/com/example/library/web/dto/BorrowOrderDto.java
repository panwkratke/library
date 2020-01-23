package com.example.library.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowOrderDto {

    private Long id;
    private String orderNumber;
    private UserDto user;
    private BookDto book;
    private String creationDate;
}

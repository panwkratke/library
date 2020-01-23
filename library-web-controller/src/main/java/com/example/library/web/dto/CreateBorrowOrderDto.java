package com.example.library.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBorrowOrderDto {

    private Long userId;
    private Long bookId;
}

package com.example.library.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {

    private Long id;
    private String title;
    private String author;
    private String genre;
    private Integer pages;
    private Integer quantity;
    private String creationDate;
}

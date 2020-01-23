package com.example.library.web.dto;

import com.example.library.model.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String token;
    private String role;
    private String creationDate;
    private Set<Book> borrowedBooks;
}

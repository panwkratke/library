package com.example.library.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Book extends AbstractEntity {

    private String title;
    private String author;
    private String genre;
    private Integer pages;
    private Integer quantity;
}

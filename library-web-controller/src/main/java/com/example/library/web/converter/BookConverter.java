package com.example.library.web.converter;

import com.example.library.model.Book;
import com.example.library.web.dto.ApiResponseDto;
import com.example.library.web.dto.BookDto;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Book converter supports all required operations regarding conversions between book objects.
 */
public class BookConverter {

    /**
     * Converts book dto to book db.
     *
     * @param dto Book dto to be converted.
     * @return Book after conversion.
     */
    public static Book dto2db(BookDto dto) {
        Book db = new Book();
        BookConverter.dto2db(db, dto);
        return db;
    }

    private static void dto2db(Book db, BookDto dto) {
        db.setId(dto.getId());
        db.setTitle(dto.getTitle());
        db.setAuthor(dto.getAuthor());
        db.setGenre(dto.getGenre());
        db.setPages(dto.getPages());
        db.setQuantity(dto.getQuantity());
    }

    /**
     * Converts book db to book dto.
     *
     * @param db Book db to be converted.
     * @return BookDto after conversion.
     */
    public static BookDto db2dto(Book db) {
        BookDto dto = new BookDto();
        BookConverter.db2dto(dto, db);
        return dto;
    }

    private static void db2dto(BookDto dto, Book db) {
        dto.setId(db.getId());
        dto.setAuthor(db.getAuthor());
        dto.setTitle(db.getTitle());
        dto.setGenre(db.getGenre());
        dto.setPages(db.getPages());
        dto.setQuantity(db.getQuantity());
        dto.setCreationDate(db.getCreationDate().getTime().toString());
    }

    /**
     * Converts a list of books db to list of books dto.
     *
     * @param dbBookList List of books db to be converted.
     * @return List of books dto after conversion.
     */
    public static List<BookDto> bookDbListToBookDtoList(List<Book> dbBookList) {
        List<BookDto> bookDtoList = new ArrayList<>();
        for (Book b : dbBookList) {
            bookDtoList.add(db2dto(b));
        }
        return bookDtoList;
    }

    /**
     * Converts book db to ResponseEntity.
     *
     * @param db Book db to be converted.
     * @return ResponseEntity containing ApiResponse with converted Book to BookDto.
     */
    public static ResponseEntity<ApiResponseDto<BookDto>> db2re(Book db) {
        BookDto dto = BookConverter.db2dto(db);
        return ResponseEntity.ok().body(ApiResponseDto.<BookDto>builder().content(dto).build());
    }

    /**
     * Converts book dto to book db for the book update.
     *
     * @param db  Book db to be prepared for the update.
     * @param dto BookDto containing a new data for the book update.
     */
    public static void dto2dbForUpdate(Book db, BookDto dto) {
        db.setAuthor(dto.getAuthor());
        db.setTitle(dto.getTitle());
        db.setGenre(dto.getGenre());
        db.setPages(dto.getPages());
        db.setQuantity(dto.getQuantity());
    }
}

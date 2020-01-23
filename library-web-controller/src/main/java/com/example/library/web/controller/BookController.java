package com.example.library.web.controller;

import com.example.library.core.exception.ActiveBorrowOrdersException;
import com.example.library.core.exception.BookExistException;
import com.example.library.core.exception.BookNotFoundException;
import com.example.library.core.service.BookService;
import com.example.library.model.Book;
import com.example.library.web.converter.ApiConverter;
import com.example.library.web.converter.BookConverter;
import com.example.library.web.dto.ApiResponseDto;
import com.example.library.web.dto.BookDto;
import com.example.library.web.dto.enums.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Book controller supports all controller operations regarding http requests for books. (REST)
 */
@Slf4j
@RestController
@RequestMapping(value = "/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Processes http post request to add a new book to the repository.
     *
     * @param dto Book dto to be processed.
     * @return ResponseEntity containing ApiResponse with saved book.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ApiResponseDto<BookDto>> addNewBook(@RequestBody BookDto dto) {
        Book book = BookConverter.dto2db(dto);
        try {
            this.bookService.addNewBook(book);
        } catch (BookExistException e) {
            log.error("Book with id: {} already exists!", book.getId(), e);
            return ApiConverter.resolveApiError(ApiError.DUPLICATED_BOOK);
        }
        return BookConverter.db2re(book);
    }

    /**
     * Processes http get request to retrieve a book by book id from the repository.
     *
     * @param id Book id to be searched.
     * @return ResponseEntity containing ApiResponse with retrieved book.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponseDto<BookDto>> getBookById(@PathVariable Long id) {
        Book book;
        try {
            book = this.bookService.getBookById(id);
        } catch (BookNotFoundException e) {
            log.error("Book with id: {} not found", id, e);
            return ApiConverter.resolveApiError(ApiError.BOOK_DOES_NOT_EXIST);
        }
        return BookConverter.db2re(book);
    }

    /**
     * Processes http get request to retrieve a book by book title and author from the repository.
     *
     * @param title  Book title to be searched.
     * @param author Book author to be searched.
     * @return ResponseEntity containing ApiResponse with retrieved book.
     */
    @RequestMapping(value = "/title/author", method = RequestMethod.GET)
    public ResponseEntity<ApiResponseDto<BookDto>> getBookByTitleAndAuthor(@RequestParam("title") String title, @RequestParam("author") String author) {
        Book book;
        try {
            book = this.bookService.getBookByTitleAndAuthor(title, author);
        } catch (BookNotFoundException e) {
            log.error("Book with title: {} and author: {} not found", title, author, e);
            return ApiConverter.resolveApiError(ApiError.BOOK_DOES_NOT_EXIST);
        }
        return BookConverter.db2re(book);
    }

    /**
     * Processes http get request to retrieve a list of all books from the repository.
     *
     * @return The list of all retrieved books.
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<BookDto> getBooks() {
        return BookConverter.bookDbListToBookDtoList(this.bookService.getBooks());
    }

    /**
     * Processes http get request to retrieve a list of all books by author from the repository.
     *
     * @param author Book author to be searched.
     * @return The list of all retrieved books by given author.
     */
    @RequestMapping(value = "/list/author/{author}", method = RequestMethod.GET)
    public List<BookDto> getBooksByAuthor(@PathVariable String author) {
        return BookConverter.bookDbListToBookDtoList(this.bookService.getBooksByAuthor(author));
    }

    /**
     * Processes http get request to retrieve a list of all books by genre from the repository.
     *
     * @param genre Book genre to be searched.
     * @return The list of all retrieved books by given genre.
     */
    @RequestMapping(value = "/list/genre/{genre}", method = RequestMethod.GET)
    public List<BookDto> getBooksByGenre(@PathVariable String genre) {
        return BookConverter.bookDbListToBookDtoList(this.bookService.getBooksByGenre(genre));
    }

    /**
     * Processes http get request to retrieve a list of all books by title from the repository.
     *
     * @param title Book title to be searched.
     * @return The list of all retrieved books by given title.
     */
    @RequestMapping(value = "/list/title/{title}", method = RequestMethod.GET)
    public List<BookDto> getBooksByTitle(@PathVariable String title) {
        return BookConverter.bookDbListToBookDtoList(this.bookService.getBooksByTitle(title));
    }

    /**
     * Processes http delete request to delete a book by book id from the repository.
     *
     * @param id Book id to be searched.
     * @return ResponseEntity containing ApiResponse without a body.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponseDto<Void>> deleteBookById(@PathVariable Long id) {
        try {
            this.bookService.deleteBookById(id);
        } catch (BookNotFoundException e) {
            log.error("Book with id: {} not found", id, e);
            return ApiConverter.resolveApiError(ApiError.BOOK_DOES_NOT_EXIST);
        } catch (ActiveBorrowOrdersException a) {
            log.error("Book with id: {} has active borrow orders", id, a);
            return ApiConverter.resolveApiError(ApiError.ACTIVE_BORROW_ORDERS);
        }
        return ResponseEntity.ok().body(ApiResponseDto.<Void>builder().build());
    }

    /**
     * Processes http put request to update a book in the repository.
     *
     * @param dto Book dto to be processed.
     * @return ResponseEntity containing ApiResponse with updated book.
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<ApiResponseDto<BookDto>> updateBook(@RequestBody BookDto dto) {
        Book book = new Book();
        try {
            book = this.bookService.getBookById(dto.getId());
            BookConverter.dto2dbForUpdate(book, dto);
            this.bookService.updateBook(book);
        } catch (BookNotFoundException e) {
            log.error("Book with id: {} not found", book.getId(), e);
            return ApiConverter.resolveApiError(ApiError.BOOK_DOES_NOT_EXIST);
        }
        return BookConverter.db2re(book);
    }
}

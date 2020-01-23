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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({BookConverter.class, ApiConverter.class})
public class BookControllerTest {

    private Long mockedId;
    private String mockedAuthor;
    private String mockedTitle;
    private String mockedGenre;
    private Book mockedBook;
    private BookDto mockedBookDto;
    private List<Book> mockedBookDbTable;
    private List<BookDto> actualBookDbTable;
    private List<Book> incorrectBookDbTable;
    private BookService bookServiceMock;
    private BookController bookController;
    private ResponseEntity<ApiResponseDto<BookDto>> actual;
    private ResponseEntity<ApiResponseDto<Void>> voidActual;

    @Before
    public void initialize() {
        mockedId = 1L;
        mockedAuthor = "mockedAuthor";
        mockedTitle = "mockedTitle";
        mockedGenre = "mockedGenre";
        mockedBook = new Book();
        mockedBook.setId(mockedId);
        mockedBook.setAuthor(mockedAuthor);
        mockedBook.setGenre(mockedGenre);
        mockedBook.setTitle(mockedTitle);
        mockedBookDto = new BookDto();
        mockedBookDto.setId(mockedId);
        mockedBookDto.setAuthor(mockedAuthor);
        mockedBookDto.setGenre(mockedGenre);
        mockedBookDto.setTitle(mockedTitle);
        mockedBookDbTable = new ArrayList<>();
        mockedBookDbTable.add(mockedBook);
        actualBookDbTable = new ArrayList<>();
        incorrectBookDbTable = new ArrayList<>();
        bookServiceMock = mock(BookService.class);
        bookController = new BookController(bookServiceMock);
        List<BookDto> mockedBookDtoTable = new ArrayList<>();
        mockedBookDtoTable.add(mockedBookDto);
        ResponseEntity<ApiResponseDto<BookDto>> mockedResponseEntity = new ResponseEntity<>(HttpStatus.OK);
        PowerMockito.mockStatic(BookConverter.class);
        BDDMockito.given(BookConverter.dto2db(any(BookDto.class))).willReturn(mockedBook);
        BDDMockito.given(BookConverter.db2dto(any(Book.class))).willReturn(mockedBookDto);
        BDDMockito.given(BookConverter.db2re(any(Book.class))).willReturn(mockedResponseEntity);
        BDDMockito.given(BookConverter.bookDbListToBookDtoList(mockedBookDbTable)).willReturn(mockedBookDtoTable);
    }

    @Test
    public void addNewBook_when_book_does_not_exists() throws BookExistException {
        //given
        doNothing().when(bookServiceMock).addNewBook(any(Book.class));

        // when
        actual = bookController.addNewBook(mockedBookDto);

        // then
        Assert.assertEquals("not OK request", HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    public void addNewBook_when_book_exist() throws BookExistException {
        //given
        doThrow(BookExistException.createWithId(mockedId)).when(bookServiceMock).addNewBook(any(Book.class));

        // when
        actual = bookController.addNewBook(mockedBookDto);

        // then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        // checking if ApiError.DUPLICATED_BOOK is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.DUPLICATED_BOOK.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.DUPLICATED_BOOK.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.DUPLICATED_BOOK.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }

    @Test
    public void getBookById_when_book_exist() throws BookNotFoundException {
        // given
        when(bookServiceMock.getBookById(mockedId)).thenReturn(mockedBook);

        // when
        actual = bookController.getBookById(mockedId);

        // then
        Assert.assertEquals("Response not equal", HttpStatus.OK, actual.getStatusCode());
    }


    @Test
    public void getBookById_when_book_does_not_exist() throws BookNotFoundException {
        //given
        doThrow(BookNotFoundException.createWithId(mockedId)).when(bookServiceMock).getBookById(mockedId);

        //when
        actual = bookController.getBookById(mockedId);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        // checking if ApiError.BOOK_DOES_NOT_EXIST is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.BOOK_DOES_NOT_EXIST.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.BOOK_DOES_NOT_EXIST.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.BOOK_DOES_NOT_EXIST.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }

    @Test
    public void getBooks_with_correct_list() {
        //given
        when(bookServiceMock.getBooks()).thenReturn(mockedBookDbTable);

        //when
        actualBookDbTable = bookController.getBooks();

        //then
        Assert.assertEquals("Incorrect array size", mockedBookDbTable.size(), actualBookDbTable.size());

        // checking if elements in both lists are equal
        for (int i = 0; i < mockedBookDbTable.size(); i++) {
            Book expectedBook = mockedBookDbTable.get(i);
            BookDto actualBook = actualBookDbTable.get(i);
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBook.getTitle(), actualBook.getTitle(), i), expectedBook.getTitle(), actualBook.getTitle());
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBook.getAuthor(), actualBook.getAuthor(), i), expectedBook.getAuthor(), actualBook.getAuthor());
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBook.getGenre(), actualBook.getGenre(), i), expectedBook.getGenre(), actualBook.getGenre());
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBook.getPages(), actualBook.getPages(), i), expectedBook.getPages(), actualBook.getPages());
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBook.getId(), actualBook.getId(), i), expectedBook.getId(), actualBook.getId());
        }
    }

    @Test
    public void getBooks_with_incorrect_list() {
        //given
        when(bookServiceMock.getBooks()).thenReturn(incorrectBookDbTable);

        //when
        actualBookDbTable = bookController.getBooks();

        //then
        Assert.assertNotEquals("Correct array size", mockedBookDbTable.size(), actualBookDbTable.size());
    }

    @Test
    public void getBooksByAuthor_when_author_exist() {
        //given
        when(bookServiceMock.getBooksByAuthor(mockedAuthor)).thenReturn(mockedBookDbTable);

        //when
        actualBookDbTable = bookController.getBooksByAuthor(mockedAuthor);

        //then
        Assert.assertEquals("Incorrect array size", mockedBookDbTable.size(), actualBookDbTable.size());

        // checking if elements in both lists are equal
        for (int i = 0; i < mockedBookDbTable.size(); i++) {
            Book expectedBook = mockedBookDbTable.get(i);
            BookDto actualBook = actualBookDbTable.get(i);
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBook.getAuthor(), actualBook.getAuthor(), i), expectedBook.getAuthor(), actualBook.getAuthor());
        }
    }

    @Test
    public void getBookListByAuthor_when_author_does_not_exist() {
        //given
        when(bookServiceMock.getBooksByAuthor(mockedAuthor)).thenReturn(incorrectBookDbTable);

        //when
        actualBookDbTable = bookController.getBooksByAuthor(mockedAuthor);

        //then
        Assert.assertNotEquals("Correct array size", mockedBookDbTable.size(), actualBookDbTable.size());
    }

    @Test
    public void getBookListByGenre_when_genre_exist() {
        //given
        when(bookServiceMock.getBooksByGenre(mockedGenre)).thenReturn(mockedBookDbTable);

        //when
        actualBookDbTable = bookController.getBooksByGenre(mockedGenre);

        //then
        Assert.assertEquals("Incorrect array size", mockedBookDbTable.size(), actualBookDbTable.size());

        // checking if elements in both lists are equal
        for (int i = 0; i < mockedBookDbTable.size(); i++) {
            Book expectedBook = mockedBookDbTable.get(i);
            BookDto actualBook = actualBookDbTable.get(i);
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBook.getGenre(), actualBook.getGenre(), i), expectedBook.getGenre(), actualBook.getGenre());
        }
    }

    @Test
    public void getBookListByGenre_when_genre_does_not_exist() {
        //given
        when(bookServiceMock.getBooksByGenre(mockedGenre)).thenReturn(incorrectBookDbTable);

        //when
        List<BookDto> actualBookDbTable = bookController.getBooksByGenre(mockedGenre);

        //then
        Assert.assertNotEquals("Correct array size", mockedBookDbTable.size(), actualBookDbTable.size());
    }

    @Test
    public void getBookListByTitle_when_title_exist() {
        //given
        when(bookServiceMock.getBooksByTitle(mockedTitle)).thenReturn(mockedBookDbTable);

        //when
        actualBookDbTable = bookController.getBooksByTitle(mockedTitle);

        //then
        Assert.assertEquals("Incorrect array size", mockedBookDbTable.size(), actualBookDbTable.size());

        // checking if elements in both lists are equal
        for (int i = 0; i < mockedBookDbTable.size(); i++) {
            Book expectedBook = mockedBookDbTable.get(i);
            BookDto actualBook = actualBookDbTable.get(i);
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBook.getTitle(), actualBook.getTitle(), i), expectedBook.getTitle(), actualBook.getTitle());
        }
    }

    @Test
    public void getBookListByTitle_when_title_does_not_exist() {
        //given
        when(bookServiceMock.getBooksByTitle(mockedTitle)).thenReturn(incorrectBookDbTable);

        //when
        List<BookDto> actualBookDbTable = bookController.getBooksByTitle(mockedTitle);

        //then
        Assert.assertNotEquals("Correct array size", mockedBookDbTable.size(), actualBookDbTable.size());
    }

    @Test
    public void updateBook_when_book_exist() throws BookNotFoundException {
        // given
        doNothing().when(bookServiceMock).updateBook(mockedBook);
        when(bookServiceMock.getBookById(any(Long.class))).thenReturn(mockedBook);

        // when
        actual = bookController.updateBook(mockedBookDto);

        // then
        Assert.assertEquals("Response not equal", HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    public void updateBook_when_book_does_not_exist() throws BookNotFoundException {
        //given
        doThrow(BookNotFoundException.createWithId(mockedId)).when(bookServiceMock).getBookById(mockedId);
        doThrow(BookNotFoundException.createWithId(mockedId)).when(bookServiceMock).updateBook(mockedBook);

        //when
        actual = bookController.updateBook(mockedBookDto);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        // checking if ApiError.BOOK_DOES_NOT_EXIST is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.BOOK_DOES_NOT_EXIST.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.BOOK_DOES_NOT_EXIST.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.BOOK_DOES_NOT_EXIST.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }

    @Test
    public void deleteBookById_when_book_exist() throws BookNotFoundException, ActiveBorrowOrdersException {
        //given
        doNothing().when(bookServiceMock).deleteBookById(any(Long.class));

        //when
        voidActual = bookController.deleteBookById(mockedId);

        //when then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.OK, voidActual.getStatusCode());
        Assert.assertNotNull("body is null", voidActual.getBody());
    }

    @Test
    public void deleteBookById_when_book_does_not_exist() throws BookNotFoundException, ActiveBorrowOrdersException {
        //given
        doThrow(BookNotFoundException.createWithId(mockedId)).when(bookServiceMock).deleteBookById(mockedId);

        //when
        voidActual = bookController.deleteBookById(mockedId);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, voidActual.getStatusCode());
        Assert.assertNotNull("body is null", voidActual.getBody());

        // checking if ApiError.BOOK_DOES_NOT_EXIST is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.BOOK_DOES_NOT_EXIST.getApiError()), voidActual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.BOOK_DOES_NOT_EXIST.getHttpStatus().value()), voidActual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.BOOK_DOES_NOT_EXIST.getApiErrorMsg(), voidActual.getBody().getApiErrorMsg());
    }

    @Test
    public void deleteBookById_when_book_has_active_borrow_orders() throws BookNotFoundException, ActiveBorrowOrdersException {
        //given
        doThrow(ActiveBorrowOrdersException.createWithUserId(mockedId)).when(bookServiceMock).deleteBookById(mockedId);

        //when
        voidActual = bookController.deleteBookById(mockedId);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, voidActual.getStatusCode());
        Assert.assertNotNull("body is null", voidActual.getBody());

        //checking if ApiError.ACTIVE_BORROW_ORDERS. is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.ACTIVE_BORROW_ORDERS.getApiError()), voidActual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.ACTIVE_BORROW_ORDERS.getHttpStatus().value()), voidActual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.ACTIVE_BORROW_ORDERS.getApiErrorMsg(), voidActual.getBody().getApiErrorMsg());
    }

    @Test
    public void getBookByTitleAndAuthor_when_book_exist() throws BookNotFoundException {
        // given
        when(bookServiceMock.getBookByTitleAndAuthor(mockedTitle, mockedAuthor)).thenReturn(mockedBook);

        // when
        actual = bookController.getBookByTitleAndAuthor(mockedTitle, mockedAuthor);

        // then
        Assert.assertEquals("Response not equal", HttpStatus.OK, actual.getStatusCode());
    }


    @Test
    public void getBookByTitleAndAuthor_when_book_does_not_exist() throws BookNotFoundException {
        //given
        doThrow(BookNotFoundException.createWithId(mockedId)).when(bookServiceMock).getBookByTitleAndAuthor(mockedTitle, mockedAuthor);

        //when
        actual = bookController.getBookByTitleAndAuthor(mockedTitle, mockedAuthor);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        // checking if ApiError.BOOK_DOES_NOT_EXIST is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.BOOK_DOES_NOT_EXIST.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.BOOK_DOES_NOT_EXIST.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.BOOK_DOES_NOT_EXIST.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }
}

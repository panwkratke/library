package com.example.library.core.service;

import com.example.library.core.exception.ActiveBorrowOrdersException;
import com.example.library.core.exception.BookExistException;
import com.example.library.core.exception.BookNotFoundException;
import com.example.library.core.exception.UserNotFoundException;
import com.example.library.core.repository.BookRepository;
import com.example.library.core.repository.BorrowOrderRepository;
import com.example.library.model.Book;
import com.example.library.model.BorrowOrder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class BookServiceTest {

    private Long mockedId;
    private Long incorrectId;
    private String mockedTitle;
    private String mockedAuthor;
    private String mockedGenre;
    private String titleDoesNotExist;
    private String authorDoesNotExist;
    private String genreDoesNotExist;
    private Book mockedBook;
    private Book actualBook;
    private BorrowOrder mockedBorrowOrder;
    private BookRepository bookRepositoryMock;
    private BorrowOrderRepository borrowOrderRepositoryMock;
    private List<Book> actualBooksDbTable;
    private List<Book> mockedBooksDbTable;
    private List<BorrowOrder> mockedBorrowOrdersDbTable;
    private Optional<Book> mockedBookOpt;
    private BookService bookService;

    @Before
    public void initialize() {
        mockedId = 1L;
        incorrectId = 2L;
        mockedTitle = "mockedTitle";
        mockedAuthor = "mockedAuthor";
        mockedGenre = "mockedGenre";
        titleDoesNotExist = "titleDoesNotExist";
        authorDoesNotExist = "authorDoesNotExist";
        genreDoesNotExist = "genreDoesNotExist";
        bookRepositoryMock = mock(BookRepository.class);
        borrowOrderRepositoryMock = mock(BorrowOrderRepository.class);
        actualBook = new Book();
        mockedBook = new Book();
        mockedBook.setId(mockedId);
        mockedBook.setTitle(mockedTitle);
        mockedBook.setAuthor(mockedAuthor);
        mockedBorrowOrder = new BorrowOrder();
        mockedBorrowOrder.setBook(mockedBook);
        actualBooksDbTable = new ArrayList<>();
        mockedBooksDbTable = new ArrayList<>();
        mockedBorrowOrdersDbTable =new ArrayList<>();
        mockedBooksDbTable.add(mockedBook);
        mockedBookOpt = Optional.of(mockedBook);
        bookService = new BookService(bookRepositoryMock, borrowOrderRepositoryMock);
    }

    @Test
    public void addNewBook_when_book_does_not_exist() {
        // given
        Book mockedNewBook = new Book();
        doAnswer(invocation -> {
            if (mockedBooksDbTable.contains(invocation.getArgument(0, Book.class))) {
                throw BookExistException.createWithId(mockedNewBook.getId());
            } else {
                mockedBooksDbTable.add(mockedNewBook);
                return null;
            }
        }).when(bookRepositoryMock).save(mockedBook);

        when(bookRepositoryMock.findByTitleAndAuthor(mockedTitle, mockedAuthor)).thenReturn(mockedBookOpt);

        // when
        try {
            bookService.addNewBook(mockedNewBook);
        } catch (BookExistException e) {
            log.error("Book already exist", e);
            Assert.fail("Book already exist");
            return;
        }

        // then
        boolean wasAdded = mockedBooksDbTable.stream().anyMatch(mockedBook::equals);
        Assert.assertTrue("Book wasn't added", wasAdded);
    }

    @Test(expected = BookExistException.class)
    public void addBook_when_book_already_exist() throws BookExistException {
        // given
        doAnswer(invocation -> {
            if (mockedBooksDbTable.contains(invocation.getArgument(0, Book.class))) {
                throw BookExistException.createWithId(mockedBook.getId());
            } else {
                mockedBooksDbTable.add(mockedBook);
                return null;
            }
        }).when(bookRepositoryMock).save(mockedBook);

        when(bookRepositoryMock.findById(mockedId)).thenReturn(mockedBookOpt);

        // when then
        bookService.addNewBook(mockedBook);
    }

    @Test
    public void getBookById_when_book_exist() {
        // given
        when(bookRepositoryMock.findById(mockedId)).thenReturn(mockedBookOpt);

        // when
        try {
            actualBook = bookService.getBookById(mockedId);
        } catch (BookNotFoundException e) {
            log.error("Book not found", e);
            Assert.fail("Book not found");
            return;
        }

        // then
        Assert.assertEquals("Book id is incorrect", mockedId, actualBook.getId());
    }

    @Test(expected = BookNotFoundException.class)
    public void getBookById_book_does_not_exist() throws BookNotFoundException {
        // given
        when(bookRepositoryMock.findById(mockedId)).thenReturn(mockedBookOpt);

        // when then
        bookService.getBookById(incorrectId);
    }

    @Test
    public void getBookByTitleAndAuthor_when_book_exist() {
        // given
        when(bookRepositoryMock.findByTitleAndAuthor(mockedTitle, mockedAuthor)).thenReturn(mockedBookOpt);

        // when
        try {
            actualBook = bookService.getBookByTitleAndAuthor(mockedTitle, mockedAuthor);
        } catch (BookNotFoundException e) {
            log.error("Book not found", e);
            Assert.fail("Book not found");
            return;
        }

        // then
        Assert.assertEquals("Title is incorrect", mockedTitle, actualBook.getTitle());
        Assert.assertEquals("Author is incorrect", mockedAuthor, actualBook.getAuthor());
    }

    @Test(expected = BookNotFoundException.class)
    public void getBookByTitleAndAuthor_when_book_does_not_exist() throws BookNotFoundException {
        // given
        when(bookRepositoryMock.findByTitleAndAuthor(mockedTitle, mockedAuthor)).thenReturn(mockedBookOpt);

        // when then
        bookService.getBookByTitleAndAuthor(titleDoesNotExist, authorDoesNotExist);
    }

    @Test
    public void deleteBookById_when_book_exist() {
        // given
        doAnswer(invocation -> {
            Long callId = invocation.getArgument(0, Long.class);
            mockedBooksDbTable.removeIf(user -> callId.equals(user.getId()));
            return null;
        }).when(bookRepositoryMock).deleteById(mockedId);

        when(bookRepositoryMock.findById(mockedId)).thenReturn(mockedBookOpt);

        // when
        try {
            bookService.deleteBookById(mockedId);
        } catch (BookNotFoundException e) {
            log.error("Book not found", e);
            Assert.fail("Book not found");
            return;
        } catch (ActiveBorrowOrdersException a) {
            log.error("Book has active borrow orders", a);
            Assert.fail("Book has active borrow orders");
            return;
        }

        // then
        boolean wasRemoved = mockedBooksDbTable.stream().noneMatch(book -> mockedId.equals(book.getId()));
        Assert.assertTrue("It wasn't removed ", wasRemoved);
    }

    @Test(expected = BookNotFoundException.class)
    public void deleteBookById_when_book_does_not_exist() throws BookNotFoundException, ActiveBorrowOrdersException {
        // given
        doAnswer(invocation -> {
            Long callId = invocation.getArgument(0, Long.class);
            mockedBooksDbTable.removeIf(book -> callId.equals(book.getId()));
            return null;
        }).when(bookRepositoryMock).deleteById(mockedId);

        when(bookRepositoryMock.findById(mockedId)).thenReturn(mockedBookOpt);

        // when then
        bookService.deleteBookById(incorrectId);
    }

    @Test(expected = ActiveBorrowOrdersException.class)
    public void deleteBookById_when_book_has_active_borrow_orders() throws BookNotFoundException, ActiveBorrowOrdersException {
        // given
        doAnswer(invocation -> {
            Long callId = invocation.getArgument(0, Long.class);
            mockedBooksDbTable.removeIf(book -> callId.equals(book.getId()));
            return null;
        }).when(bookRepositoryMock).deleteById(mockedId);

        when(bookRepositoryMock.findById(mockedId)).thenReturn(mockedBookOpt);
        mockedBorrowOrdersDbTable.add(mockedBorrowOrder);
        when(borrowOrderRepositoryMock.findAllByBookId(mockedId)).thenReturn(mockedBorrowOrdersDbTable);

        // when then
        bookService.deleteBookById(mockedId);
    }

    @Test
    public void updateBookById_when_book_exist() {
        // given
        doAnswer(invocation -> {
            if (!mockedBooksDbTable.contains(invocation.getArgument(0, Book.class))) {
                throw UserNotFoundException.createWithId(mockedBook.getId());
            } else {
                mockedBooksDbTable.add(0, mockedBook);
                return null;
            }
        }).when(bookRepositoryMock).save(mockedBook);

        when(bookRepositoryMock.findById(mockedId)).thenReturn(mockedBookOpt);

        // when
        try {
            bookService.updateBook(mockedBook);
        } catch (BookNotFoundException e) {
            log.error("Book not found", e);
            Assert.fail("Book not found");
            return;
        }

        // then
        boolean wasUpdated = mockedBooksDbTable.stream().anyMatch(mockedBook::equals);
        Assert.assertTrue("Book wasn't registered", wasUpdated);
    }

    @Test(expected = BookNotFoundException.class)
    public void updateBookById_when_book_does_not_exist() throws BookNotFoundException {
        // given
        doAnswer(invocation -> {
            if (!mockedBooksDbTable.contains(invocation.getArgument(0, Book.class))) {
                throw UserNotFoundException.createWithId(mockedBook.getId());
            } else {
                mockedBooksDbTable.add(0, mockedBook);
                return null;
            }
        }).when(bookRepositoryMock).save(mockedBook);

        when(bookRepositoryMock.findById(incorrectId)).thenReturn(mockedBookOpt);

        // when then
        bookService.updateBook(mockedBook);
    }

    @Test
    public void getBooks_with_correct_list_size() {
        // given
        when(bookRepositoryMock.findAllBooks()).thenReturn(mockedBooksDbTable);

        // when
        actualBooksDbTable = bookService.getBooks();

        // then
        Assert.assertEquals("Incorrect array size", mockedBooksDbTable.size(), actualBooksDbTable.size());

        //checking if elements in both lists are equal
        for (int i = 0; i < mockedBooksDbTable.size(); i++) {
            Book expectedBook = mockedBooksDbTable.get(i);
            Book actualBook = actualBooksDbTable.get(i);
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBook, actualBook, i),expectedBook, actualBook);
        }
    }

    @Test
    public void getBooks_with_incorrect_list_size() {
        // given
        List<Book> incorrectBookDbTable = new ArrayList<>();
        when(bookRepositoryMock.findAllBooks()).thenReturn(mockedBooksDbTable);

        // when
        actualBooksDbTable = bookService.getBooks();

        // then
        Assert.assertNotEquals("Correct array size", incorrectBookDbTable.size(), actualBooksDbTable.size());

    }

    @Test
    public void getBooksByAuthor_when_author_exist() {
        // given
        when(bookRepositoryMock.findAllByAuthor(mockedAuthor)).thenReturn(mockedBooksDbTable);

        // when
        actualBooksDbTable = bookService.getBooksByAuthor(mockedAuthor);

        // then
        Assert.assertEquals("Incorrect array size", mockedBooksDbTable.size(), actualBooksDbTable.size());

        //checking if elements in both lists are equal
        for (int i = 0; i < mockedBooksDbTable.size(); i++) {
            Book expectedBook = mockedBooksDbTable.get(i);
            Book actualBook = actualBooksDbTable.get(i);
            Assert.assertEquals(String.format("Book author %s is not equal actual %s for i = %d", expectedBook.getAuthor(), actualBook.getAuthor(), i),expectedBook.getAuthor(), actualBook.getAuthor());
        }
    }

    @Test
    public void getBooksByAuthor_when_author_does_not_exist() {
        // given
        when(bookRepositoryMock.findAllByAuthor(mockedAuthor)).thenReturn(mockedBooksDbTable);

        // when
        actualBooksDbTable = bookService.getBooksByAuthor(authorDoesNotExist);

        // then
        Assert.assertNotEquals("Incorrect array size", mockedBooksDbTable.size(), actualBooksDbTable.size());
    }

    @Test
    public void getBooksByGenre_when_genre_exist() {
        // given
        when(bookRepositoryMock.findAllByGenre(mockedGenre)).thenReturn(mockedBooksDbTable);


        // when
        actualBooksDbTable = bookService.getBooksByGenre(mockedGenre);

        // then
        Assert.assertEquals("Incorrect array size", mockedBooksDbTable.size(), actualBooksDbTable.size());

        //checking if elements in both lists are equal
        for (int i = 0; i < mockedBooksDbTable.size(); i++) {
            Book expectedBook = mockedBooksDbTable.get(i);
            Book actualBook = actualBooksDbTable.get(i);
            Assert.assertEquals(String.format("Book genre %s is not equal actual %s for i = %d", expectedBook.getGenre(), actualBook.getGenre(), i),expectedBook.getGenre(), actualBook.getGenre());
        }
    }

    @Test
    public void getBooksByGenre_when_genre_does_not_exist() {
        // given
        when(bookRepositoryMock.findAllByGenre(mockedGenre)).thenReturn(mockedBooksDbTable);

        // when
        actualBooksDbTable = bookService.getBooksByGenre(genreDoesNotExist);

        // then
        Assert.assertNotEquals("Incorrect array size", mockedBooksDbTable.size(), actualBooksDbTable.size());
    }

    @Test
    public void getBooksByTitle_when_title_exist() {
        // given
        when(bookRepositoryMock.findAllByTitle(mockedTitle)).thenReturn(mockedBooksDbTable);


        // when
        actualBooksDbTable = bookService.getBooksByTitle(mockedTitle);

        // then
        Assert.assertEquals("Incorrect array size", mockedBooksDbTable.size(), actualBooksDbTable.size());

        //checking if elements in both lists are equal
        for (int i = 0; i < mockedBooksDbTable.size(); i++) {
            Book expectedBook = mockedBooksDbTable.get(i);
            Book actualBook = actualBooksDbTable.get(i);
            Assert.assertEquals(String.format("Book genre %s is not equal actual %s for i = %d", expectedBook.getTitle(), actualBook.getTitle(), i),expectedBook.getTitle(), actualBook.getTitle());
        }
    }

    @Test
    public void getBooksByTitle_when_title_does_not_exist() {
        // given
        when(bookRepositoryMock.findAllByTitle(mockedTitle)).thenReturn(mockedBooksDbTable);

        // when
        actualBooksDbTable = bookService.getBooksByTitle(titleDoesNotExist);

        // then
        Assert.assertNotEquals("Incorrect array size", mockedBooksDbTable.size(), actualBooksDbTable.size());
    }
}

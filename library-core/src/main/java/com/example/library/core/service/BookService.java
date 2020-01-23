package com.example.library.core.service;

import com.example.library.core.exception.ActiveBorrowOrdersException;
import com.example.library.core.exception.BookExistException;
import com.example.library.core.exception.BookNotFoundException;
import com.example.library.core.repository.BookRepository;
import com.example.library.core.repository.BorrowOrderRepository;
import com.example.library.model.Book;
import com.example.library.model.BorrowOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Book service supports all operations regarding books. (CRUD)
 */
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BorrowOrderRepository borrowOrderRepository;

    @Autowired
    public BookService(BookRepository bookRepository, BorrowOrderRepository borrowOrderRepository) {
        this.bookRepository = bookRepository;
        this.borrowOrderRepository = borrowOrderRepository;
    }

    /**
     * Adds a new Book to the repository.
     *
     * @param book New book to be saved in the book repository.
     * @throws BookExistException Book with given title already exist in the repository.
     */
    public void addNewBook(Book book) throws BookExistException {
        Optional<Book> bookOptional = this.bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
        if (bookOptional.isEmpty()) {
            this.bookRepository.save(book);
        } else {
            throw BookExistException.createWithId(book.getId());
        }
    }

    /**
     * Retrieves book from the repository by id.
     *
     * @param id Book id to be searched in the repository.
     * @return Book from the book repository with the given id.
     * @throws BookNotFoundException The book with the given id does not exist in the repository.
     */
    public Book getBookById(Long id) throws BookNotFoundException {
        Optional<Book> bookOptional = this.bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw BookNotFoundException.createWithId(id);
        }
        return bookOptional.get();
    }

    /**
     * Retrieves book from the repository by title and author.
     *
     * @param title  Book title to be searched in the repository.
     * @param author Book author to be searched in the repository.
     * @return Book from the book repository with the given title and author.
     * @throws BookNotFoundException The book with the given title and author does not exist in the repository.
     */
    public Book getBookByTitleAndAuthor(String title, String author) throws BookNotFoundException {
        Optional<Book> bookOptional = this.bookRepository.findByTitleAndAuthor(title, author);
        if (bookOptional.isEmpty()) {
            throw BookNotFoundException.createWithTitleAndAuthor(title, author);
        }
        return bookOptional.get();
    }

    /**
     * Deletes the book from the repository.
     *
     * @param id Id of the book to be deleted from the repository.
     * @throws BookNotFoundException       The book with the given id does not exist in the repository.
     * @throws ActiveBorrowOrdersException The book with the given id has active borrow orders.
     */
    public void deleteBookById(Long id) throws BookNotFoundException, ActiveBorrowOrdersException {
        Optional<Book> bookOptional = this.bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            List<BorrowOrder> borrowOrders = this.borrowOrderRepository.findAllByBookId(id);
            if (!borrowOrders.isEmpty()) {
                throw ActiveBorrowOrdersException.createWithBookId(id);
            }
            this.bookRepository.deleteById(id);
        } else {
            throw BookNotFoundException.createWithId(id);
        }
    }

    /**
     * Updates the book.
     *
     * @param book Updated book to be saved in the repository.
     * @throws BookNotFoundException The book with the given id does not exist in the repository.
     */
    public void updateBook(Book book) throws BookNotFoundException {
        Optional<Book> bookOptional = this.bookRepository.findById(book.getId());
        if (bookOptional.isEmpty()) {
            throw BookNotFoundException.createWithId(book.getId());
        }
        this.bookRepository.save(book);
    }

    /**
     * Retrieves the list of all books from the repository.
     *
     * @return Book list from the repository.
     */
    public List<Book> getBooks() {
        return this.bookRepository.findAllBooks();
    }

    /**
     * Retrieves the list of books by book author from the repository.
     *
     * @param author Book author to be searched in the repository.
     * @return Book list by author from the repository.
     */
    public List<Book> getBooksByAuthor(String author) {
        return this.bookRepository.findAllByAuthor(author);
    }

    /**
     * Retrieves the list of books by book genre from the repository.
     *
     * @param genre Book genre to be searched in the repository.
     * @return Book list by genre from the repository.
     */
    public List<Book> getBooksByGenre(String genre) {
        return this.bookRepository.findAllByGenre(genre);
    }

    /**
     * Retrieves the list of books by book title from the repository.
     *
     * @param title Book title to be searched in the repository.
     * @return Book list by title from the repository.
     */
    public List<Book> getBooksByTitle(String title) {
        return this.bookRepository.findAllByTitle(title);
    }
}

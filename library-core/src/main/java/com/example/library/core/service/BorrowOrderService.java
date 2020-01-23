package com.example.library.core.service;

import com.example.library.core.exception.*;
import com.example.library.core.repository.BookRepository;
import com.example.library.core.repository.BorrowOrderRepository;
import com.example.library.core.repository.UserRepository;
import com.example.library.model.Book;
import com.example.library.model.BorrowOrder;
import com.example.library.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Borrow order service supports all operations regarding borrow orders. (CRUD)
 */
@Service
public class BorrowOrderService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowOrderRepository borrowOrderRepository;

    @Autowired
    public BorrowOrderService(UserRepository userRepository, BookRepository bookRepository, BorrowOrderRepository borrowOrderRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.borrowOrderRepository = borrowOrderRepository;
    }

    /**
     * Creates a new borrow order. (User borrows a book)
     *
     * @param userId Id of the user who creates the borrow order.
     * @param bookId Id of the book to be borrowed.
     * @return Created borrow order.
     * @throws BorrowOrderExistException         Borrow order with the given user id and book id already exist in the repository.
     * @throws UserNotFoundException             The user with the given id does not exist in the repository.
     * @throws BookNotFoundException             The book with the given id does not exist in the repository.
     * @throws BookInsufficientQuantityException The book has insufficient quantity.
     */
    public BorrowOrder createBorrowOrder(Long userId, Long bookId) throws BorrowOrderExistException, UserNotFoundException,
            BookNotFoundException, BookInsufficientQuantityException {
        Optional<BorrowOrder> borrowOrderOptional = this.borrowOrderRepository.findByBookIdAndUserId(bookId, userId);
        if (borrowOrderOptional.isPresent()) {
            throw BorrowOrderExistException.createWithBookIdAndUserId(bookId, userId);
        }
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw UserNotFoundException.createWithId(userId);
        }
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw BookNotFoundException.createWithId(bookId);
        }
        BorrowOrder borrowOrder = prepareBorrowOrderToSave(userOptional.get(), bookOptional.get());

        return borrowOrderRepository.save(borrowOrder);
    }

    private BorrowOrder prepareBorrowOrderToSave(User user, Book book) throws BookInsufficientQuantityException {
        Book preparedBook = prepareBookToSaveBorrowOrder(book);
        User preparedUser = prepareUserToSaveBorrowOrder(user, preparedBook);
        BorrowOrder borrowOrder = new BorrowOrder();
        borrowOrder.setBook(preparedBook);
        borrowOrder.setUser(preparedUser);
        borrowOrder.setOrderNumber(UUID.randomUUID().toString());
        return borrowOrder;
    }

    private Book prepareBookToSaveBorrowOrder(Book book) throws BookInsufficientQuantityException {
        int bookQuantity = book.getQuantity();
        if (bookQuantity <= 0) {
            throw BookInsufficientQuantityException.createWithId(book.getId());
        }

        book.setQuantity(bookQuantity - 1);
        return this.bookRepository.save(book);
    }

    private User prepareUserToSaveBorrowOrder(User user, Book book) {
        user.getBorrowedBooks().add(book);
        return this.userRepository.save(user);
    }

    /**
     * Deletes the borrow order from the repository. (User returns the book)
     *
     * @param borrowOrderId Id of the borrow order to be deleted from the repository.
     * @throws BorrowOrderNotFoundException The borrow order with the given id does not exist in the repository.
     */
    public void deleteBorrowOrder(Long borrowOrderId) throws BorrowOrderNotFoundException {
        Optional<BorrowOrder> borrowOrderOptional = this.borrowOrderRepository.findById(borrowOrderId);
        if (borrowOrderOptional.isEmpty()) {
            throw BorrowOrderNotFoundException.createWithId(borrowOrderId);
        }
        prepareBorrowOrderToDelete(borrowOrderOptional.get());
        this.borrowOrderRepository.deleteById(borrowOrderOptional.get().getId());
    }

    private void prepareBorrowOrderToDelete(BorrowOrder borrowOrder) {
        Book book = borrowOrder.getBook();
        prepareBookToDeleteBorrowOrder(book);
        User user = borrowOrder.getUser();
        prepareUserToDeleteBorrowOrder(user, book);
    }

    private void prepareBookToDeleteBorrowOrder(Book book) {
        int bookQuantity = book.getQuantity();
        book.setQuantity(bookQuantity + 1);
        this.bookRepository.save(book);
    }

    private void prepareUserToDeleteBorrowOrder(User user, Book book) {
        user.getBorrowedBooks().removeIf(b -> b.getId().equals(book.getId()));
        this.userRepository.save(user);
    }

    /**
     * Retrieves the borrow order from the repository by id.
     *
     * @param id Borrow Order id to be searched in the repository.
     * @return Borrow Order from the borrow order repository with the given id.
     * @throws BorrowOrderNotFoundException The borrow order with the given id does not exist in the repository.
     */
    public BorrowOrder getBorrowOrderById(Long id) throws BorrowOrderNotFoundException {
        Optional<BorrowOrder> borrowOrderOptional = this.borrowOrderRepository.findById(id);
        if (borrowOrderOptional.isEmpty()) {
            throw BorrowOrderNotFoundException.createWithId(id);
        }
        return borrowOrderOptional.get();
    }

    /**
     * Retrieves the list of all borrow orders from the repository.
     *
     * @return Borrow order list from the repository.
     */
    public List<BorrowOrder> getBorrowOrders() {
        return borrowOrderRepository.findAll();
    }

    /**
     * Retrieves the list of borrow orders by user id from the repository.
     *
     * @param id User id to be searched in the borrow order repository.
     * @return Borrow order list by user id from the repository.
     */
    public List<BorrowOrder> getBorrowOrdersByUserId(Long id) {
        return borrowOrderRepository.findAllByUserId(id);
    }

    /**
     * Retrieves the list of borrow orders by book id from the repository.
     *
     * @param id Book id to be searched in the borrow order repository.
     * @return Borrow order list by book id from the repository.
     */
    public List<BorrowOrder> getBorrowOrdersByBookId(Long id) {
        return borrowOrderRepository.findAllByBookId(id);
    }
}

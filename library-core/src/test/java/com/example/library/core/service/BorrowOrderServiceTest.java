package com.example.library.core.service;

import com.example.library.core.exception.*;
import com.example.library.core.repository.BookRepository;
import com.example.library.core.repository.BorrowOrderRepository;
import com.example.library.core.repository.UserRepository;
import com.example.library.model.Book;
import com.example.library.model.BorrowOrder;
import com.example.library.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Mockito.*;


@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class BorrowOrderServiceTest {

    private Long mockedId;
    private Long incorrectId;
    private BorrowOrder mockedBorrowOrder;
    private BorrowOrder actualBorrowOrder;
    private BorrowOrderRepository borrowOrderRepositoryMock;
    private List<BorrowOrder> mockedBorrowOrdersDbTable;
    private List<BorrowOrder> actualBorrowOrdersDbTable;
    private Optional<BorrowOrder> mockedBorrowOrderOpt;
    private Optional<BorrowOrder> mockedEmptyBorrowOrderOpt;
    private Optional<User> mockedUserOpt;
    private Optional<Book> mockedBookOpt;
    private UserRepository userRepositoryMock;
    private BookRepository bookRepositoryMock;
    private BorrowOrderService borrowOrderService;

    @Before
    public void initialize() {
        mockedId = 1L;
        incorrectId = 2L;
        Integer mockedQuantity = 2;
        Set<Book> mockedUserBookSet = new HashSet<>();
        User mockedUser = new User();
        mockedUser.setId(mockedId);
        Book mockedBook = new Book();
        mockedBook.setId(mockedId);
        mockedBook.setQuantity(mockedQuantity);
        mockedUserBookSet.add(mockedBook);
        mockedUser.setBorrowedBooks(mockedUserBookSet);
        String mockedOrderNumber = "expectedOrderNumber";
        actualBorrowOrder = new BorrowOrder();
        mockedBorrowOrder = new BorrowOrder();
        mockedBorrowOrder.setId(mockedId);
        mockedBorrowOrder.setOrderNumber(mockedOrderNumber);
        mockedBorrowOrder.setBook(mockedBook);
        mockedBorrowOrder.setUser(mockedUser);
        mockedBorrowOrderOpt = Optional.of(mockedBorrowOrder);
        mockedEmptyBorrowOrderOpt = Optional.empty();
        borrowOrderRepositoryMock = mock(BorrowOrderRepository.class);
        mockedBorrowOrdersDbTable = new ArrayList<>();
        mockedBorrowOrdersDbTable.add(mockedBorrowOrder);
        actualBorrowOrdersDbTable = new ArrayList<>();
        mockedUserOpt = Optional.of(mockedUser);
        mockedBookOpt = Optional.of(mockedBook);
        userRepositoryMock = mock(UserRepository.class);
        bookRepositoryMock = mock(BookRepository.class);
        borrowOrderService = new BorrowOrderService(userRepositoryMock, bookRepositoryMock, borrowOrderRepositoryMock);
    }

    @Test
    public void createBorrowOrder_when_borrow_order_does_not_exist() {
        // given
        Integer quantityBefore = mockedBookOpt.get().getQuantity();

        doAnswer(invocation -> {
            if (mockedBorrowOrdersDbTable.contains(invocation.getArgument(0, BorrowOrder.class))) {
                throw BorrowOrderExistException.createWithId(mockedBorrowOrder.getId());
            } else {
                mockedBorrowOrdersDbTable.add(mockedBorrowOrder);
                return null;
            }
        }).when(borrowOrderRepositoryMock).save(any(BorrowOrder.class));

        when(borrowOrderRepositoryMock.findByBookIdAndUserId(any(Long.class), any(Long.class))).thenReturn(mockedEmptyBorrowOrderOpt);
        when(userRepositoryMock.findById(any(Long.class))).thenReturn(mockedUserOpt);
        when(bookRepositoryMock.findById(any(Long.class))).thenReturn(mockedBookOpt);

        // when
        try {
            borrowOrderService.createBorrowOrder(mockedUserOpt.get().getId(), mockedBookOpt.get().getId());
        } catch (BorrowOrderExistException bo) {
            log.error("Borrow order already exist", bo);
            Assert.fail("Borrow order already exist");
        } catch (UserNotFoundException u) {
            log.error("User not found", u);
            Assert.fail("User not found");
        } catch (BookNotFoundException b) {
            log.error("Book not found", b);
            Assert.fail("Book not found");
        } catch (BookInsufficientQuantityException q) {
            log.error("Book insufficient quantity", q);
            Assert.fail("Book insufficient quantity");
        }

        // then
        boolean wasAdded = mockedBorrowOrdersDbTable.stream().anyMatch(mockedBorrowOrder::equals);
        Assert.assertTrue("Borrow order wasn't added", wasAdded);
        Integer quantityAfter = mockedBorrowOrdersDbTable.stream().filter(bo -> bo.getId().equals(mockedBorrowOrder.getId())).findFirst().get().getBook().getQuantity();
        Assert.assertNotEquals("Quantity of the book should be one less", quantityBefore, quantityAfter);
    }

    @Test(expected = BorrowOrderExistException.class)
    public void createBorrowOrder_when_borrow_order_exist() throws BorrowOrderExistException {
        // given
        when(borrowOrderRepositoryMock.findByBookIdAndUserId(any(Long.class), any(Long.class))).thenReturn(mockedBorrowOrderOpt);
        when(bookRepositoryMock.findById(any(Long.class))).thenReturn(mockedBookOpt);
        when(userRepositoryMock.findById(any(Long.class))).thenReturn(mockedUserOpt);

        // when then
        try {
            borrowOrderService.createBorrowOrder(mockedUserOpt.get().getId(), mockedBookOpt.get().getId());
        } catch (UserNotFoundException u) {
            log.error("User not found", u);
            Assert.fail("User not found");
        } catch (BookNotFoundException b) {
            log.error("Book not found", b);
            Assert.fail("Book not found");
        } catch (BookInsufficientQuantityException q) {
            log.error("Book insufficient quantity", q);
            Assert.fail("Book insufficient quantity");
        }
    }

    @Test(expected = UserNotFoundException.class)
    public void createBorrowOrder_when_user_does_not_exist() throws UserNotFoundException {
        // given
        when(borrowOrderRepositoryMock.findByBookIdAndUserId(any(Long.class), any(Long.class))).thenReturn(mockedEmptyBorrowOrderOpt);
        when(bookRepositoryMock.findById(any(Long.class))).thenReturn(mockedBookOpt);

        // when then
        try {
            borrowOrderService.createBorrowOrder(mockedUserOpt.get().getId(), mockedBookOpt.get().getId());
        } catch (BorrowOrderExistException bo) {
            log.error("Borrow order already exist", bo);
            Assert.fail("Borrow order already exist");
        } catch (BookNotFoundException b) {
            log.error("Book not found", b);
            Assert.fail("Book not found");
        } catch (BookInsufficientQuantityException q) {
            log.error("Book insufficient quantity", q);
            Assert.fail("Book insufficient quantity");
        }
    }

    @Test(expected = BookNotFoundException.class)
    public void createBorrowOrder_when_book_does_not_exist() throws BookNotFoundException {
        // given
        when(borrowOrderRepositoryMock.findByBookIdAndUserId(any(Long.class), any(Long.class))).thenReturn(mockedEmptyBorrowOrderOpt);
        when(userRepositoryMock.findById(any(Long.class))).thenReturn(mockedUserOpt);

        // when then
        try {
            borrowOrderService.createBorrowOrder(mockedUserOpt.get().getId(), mockedBookOpt.get().getId());
        } catch (BorrowOrderExistException bo) {
            log.error("Borrow order already exist", bo);
            Assert.fail("Borrow order already exist");
        } catch (UserNotFoundException u) {
            log.error("User not found", u);
            Assert.fail("User not found");
        } catch (BookInsufficientQuantityException q) {
            log.error("Book insufficient quantity", q);
            Assert.fail("Book insufficient quantity");
        }
    }

    @Test
    public void deleteBorrowOrder_when_borrow_order_exist() {
        //given
        Integer quantityBefore = mockedBookOpt.get().getQuantity();
        doAnswer(invocation -> {
            Long callId = invocation.getArgument(0, Long.class);
            mockedBorrowOrdersDbTable.removeIf(borrowOrder -> callId.equals(borrowOrder.getId()));
            return null;
        }).when(borrowOrderRepositoryMock).deleteById(mockedId);
        when(borrowOrderRepositoryMock.findById(any(Long.class))).thenReturn(mockedBorrowOrderOpt);

        //when
        try {
            borrowOrderService.deleteBorrowOrder(mockedBorrowOrder.getId());
        } catch (BorrowOrderNotFoundException e) {
            log.error("Borrow order not found", e);
            Assert.fail("Borrow order not found");
        }

        //then
        boolean wasRemoved = mockedBorrowOrdersDbTable.stream().noneMatch(borrowOrder -> mockedId.equals(borrowOrder.getId()));
        Assert.assertTrue("It wasn't removed ", wasRemoved);
        Integer quantityAfter = mockedBorrowOrder.getBook().getQuantity();
        Assert.assertEquals("Quantity of the book should be one less", Integer.valueOf(quantityBefore + 1), quantityAfter);
    }

    @Test(expected = BorrowOrderNotFoundException.class)
    public void deleteBorrowOrder_when_borrow_order_does_not_exist() throws BorrowOrderNotFoundException {
        // given
        doAnswer(invocation -> {
            Long callId = invocation.getArgument(0, Long.class);
            mockedBorrowOrdersDbTable.removeIf(borrowOrder -> callId.equals(borrowOrder.getId()));
            return null;
        }).when(borrowOrderRepositoryMock).deleteById(mockedId);

        // when then
        borrowOrderService.deleteBorrowOrder(incorrectId);
    }

    @Test
    public void getBorrowOrderById_when_borrow_order_exist() {
        //given
        when(borrowOrderRepositoryMock.findById(any(Long.class))).thenReturn(mockedBorrowOrderOpt);

        //when
        try {
            actualBorrowOrder = borrowOrderService.getBorrowOrderById(any(Long.class));
        } catch (BorrowOrderNotFoundException e) {
            log.error("Borrow order not found", e);
            Assert.fail("Borrow order not found");
        }

        //then
        Assert.assertEquals("Borrow order id is incorrect!", mockedBorrowOrder.getId(), actualBorrowOrder.getId());
    }

    @Test(expected = BorrowOrderNotFoundException.class)
    public void getBorrowOrderById_when_borrow_order_does_not_exist() throws BorrowOrderNotFoundException {
        //given
        Long incorrectId = 2L;
        when(borrowOrderRepositoryMock.findById(mockedId)).thenReturn(mockedBorrowOrderOpt);

        //when then
        actualBorrowOrder = borrowOrderService.getBorrowOrderById(incorrectId);
    }

    @Test
    public void getBorrowOrders_with_correct_list() {
        //given
        when(borrowOrderRepositoryMock.findAll()).thenReturn(mockedBorrowOrdersDbTable);

        //when
        actualBorrowOrdersDbTable = borrowOrderService.getBorrowOrders();

        //then
        Assert.assertEquals("Incorrect array size", mockedBorrowOrdersDbTable.size(), actualBorrowOrdersDbTable.size());

        //checking if elements from expected list equals elements from actual list
        for (int i = 0; i < mockedBorrowOrdersDbTable.size(); i++) {
            BorrowOrder expectedBorrowOrder = mockedBorrowOrdersDbTable.get(i);
            BorrowOrder actualBorrowOrder = actualBorrowOrdersDbTable.get(i);
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBorrowOrder, actualBorrowOrder, i), expectedBorrowOrder, actualBorrowOrder);
        }
    }

    @Test
    public void getBorrowOrders_with_incorrect_list() {
        //given
        List<BorrowOrder> incorrectBorrowOrdersDbTable = new ArrayList<>();
        when(borrowOrderRepositoryMock.findAll()).thenReturn(incorrectBorrowOrdersDbTable);

        //when
        actualBorrowOrdersDbTable = borrowOrderService.getBorrowOrders();

        //then
        Assert.assertNotEquals("Correct array size", mockedBorrowOrdersDbTable.size(), actualBorrowOrdersDbTable.size());
    }

    @Test
    public void getBorrowOrdersByUserId_with_correct_list() {
        //given
        when(borrowOrderRepositoryMock.findAllByUserId(any(Long.class))).thenReturn(mockedBorrowOrdersDbTable);

        //when
        actualBorrowOrdersDbTable = borrowOrderService.getBorrowOrdersByUserId(mockedId);

        //then
        Assert.assertEquals("Incorrect array size", mockedBorrowOrdersDbTable.size(), actualBorrowOrdersDbTable.size());

        //checking if elements from expected list equals elements from actual list
        for (int i = 0; i < mockedBorrowOrdersDbTable.size(); i++) {
            BorrowOrder expectedBorrowOrder = mockedBorrowOrdersDbTable.get(i);
            BorrowOrder actualBorrowOrder = actualBorrowOrdersDbTable.get(i);
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBorrowOrder, actualBorrowOrder, i), expectedBorrowOrder, actualBorrowOrder);
        }
    }

    @Test
    public void getBorrowOrdersByUserId_with_incorrect_list() {
        //given
        List<BorrowOrder> incorrectBorrowOrdersDbTable = new ArrayList<>();
        when(borrowOrderRepositoryMock.findAllByUserId(any(Long.class))).thenReturn(incorrectBorrowOrdersDbTable);

        //when
        actualBorrowOrdersDbTable = borrowOrderService.getBorrowOrdersByUserId(mockedId);

        //then
        Assert.assertNotEquals("Correct array size", mockedBorrowOrdersDbTable.size(), actualBorrowOrdersDbTable.size());
    }

    @Test
    public void getBorrowOrdersByBookId_with_correct_list() {
        //given
        when(borrowOrderRepositoryMock.findAllByBookId(any(Long.class))).thenReturn(mockedBorrowOrdersDbTable);

        //when
        actualBorrowOrdersDbTable = borrowOrderService.getBorrowOrdersByBookId(mockedId);

        //then
        Assert.assertEquals("Incorrect array size", mockedBorrowOrdersDbTable.size(), actualBorrowOrdersDbTable.size());

        //checking if elements from expected list equals elements from actual list
        for (int i = 0; i < mockedBorrowOrdersDbTable.size(); i++) {
            BorrowOrder expectedBorrowOrder = mockedBorrowOrdersDbTable.get(i);
            BorrowOrder actualBorrowOrder = actualBorrowOrdersDbTable.get(i);
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBorrowOrder, actualBorrowOrder, i), expectedBorrowOrder, actualBorrowOrder);
        }
    }

    @Test
    public void getBorrowOrdersByBookId_with_incorrect_list() {
        //given
        List<BorrowOrder> incorrectBorrowOrdersDbTable = new ArrayList<>();
        when(borrowOrderRepositoryMock.findAllByBookId(any(Long.class))).thenReturn(incorrectBorrowOrdersDbTable);

        //when
        actualBorrowOrdersDbTable = borrowOrderService.getBorrowOrdersByBookId(mockedId);

        //then
        Assert.assertNotEquals("Correct array size", mockedBorrowOrdersDbTable.size(), actualBorrowOrdersDbTable.size());
    }
}

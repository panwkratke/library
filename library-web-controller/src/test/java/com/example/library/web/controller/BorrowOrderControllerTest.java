package com.example.library.web.controller;

import com.example.library.core.exception.*;
import com.example.library.core.service.BorrowOrderService;
import com.example.library.model.Book;
import com.example.library.model.BorrowOrder;
import com.example.library.model.User;
import com.example.library.web.converter.ApiConverter;
import com.example.library.web.converter.BookConverter;
import com.example.library.web.converter.BorrowOrderConverter;
import com.example.library.web.converter.UserConverter;
import com.example.library.web.dto.ApiResponseDto;
import com.example.library.web.dto.BorrowOrderDto;
import com.example.library.web.dto.CreateBorrowOrderDto;
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
@PrepareForTest({BorrowOrderConverter.class, ApiConverter.class, UserConverter.class, BookConverter.class})
public class BorrowOrderControllerTest {

    private Long mockedId;
    private BorrowOrder mockedBorrowOrder;
    private List<BorrowOrder> mockedBorrowOrderDbTable;
    private List<BorrowOrderDto> actualBorrowOrderDbTable;
    private CreateBorrowOrderDto mockedCreateBorrowOrderDto;
    private BorrowOrderService borrowOrderServiceMock;
    private BorrowOrderController borrowOrderController;
    private ResponseEntity<ApiResponseDto<BorrowOrderDto>> actual;
    private ResponseEntity<ApiResponseDto<Void>> voidActual;

    @Before
    public void initialize() {
        mockedId = 1L;
        User mockedUser = new User();
        mockedUser.setId(mockedId);
        Book mockedBook = new Book();
        mockedBook.setId(mockedId);
        mockedBorrowOrder = new BorrowOrder();
        mockedBorrowOrder.setUser(mockedUser);
        mockedBorrowOrder.setBook(mockedBook);
        BorrowOrderDto mockedBorrowOrderDto = new BorrowOrderDto();
        mockedCreateBorrowOrderDto = new CreateBorrowOrderDto();
        mockedCreateBorrowOrderDto.setUserId(mockedUser.getId());
        mockedCreateBorrowOrderDto.setBookId(mockedBook.getId());
        borrowOrderServiceMock = mock(BorrowOrderService.class);
        borrowOrderController = new BorrowOrderController(borrowOrderServiceMock);
        mockedBorrowOrderDbTable = new ArrayList<>();
        mockedBorrowOrderDbTable.add(mockedBorrowOrder);
        List<BorrowOrderDto> mockedBorrowOrderDtoTable = new ArrayList<>();
        mockedBorrowOrderDtoTable.add(mockedBorrowOrderDto);
        actualBorrowOrderDbTable = new ArrayList<>();
        mockedBorrowOrder.setUser(mockedUser);
        mockedBorrowOrder.setBook(mockedBook);
        ResponseEntity<ApiResponseDto<BorrowOrderDto>> mockedBorrowOrderResponseEntity = new ResponseEntity<>(HttpStatus.OK);
        PowerMockito.mockStatic(BorrowOrderConverter.class);
        BDDMockito.given(BorrowOrderConverter.db2re(any(BorrowOrder.class))).willReturn(mockedBorrowOrderResponseEntity);
        BDDMockito.given(BorrowOrderConverter.db2dto(any(BorrowOrder.class))).willReturn(mockedBorrowOrderDto);
        BDDMockito.given(BorrowOrderConverter.borrowOrderDbListToBorrowOrderDtoList(mockedBorrowOrderDbTable)).willReturn(mockedBorrowOrderDtoTable);
    }

    @Test
    public void createBorrowOrder_when_borrow_order_does_not_exist() throws BorrowOrderExistException, UserNotFoundException, BookNotFoundException, BookInsufficientQuantityException {
        //given
        when(borrowOrderServiceMock.createBorrowOrder(any(Long.class), any(Long.class))).thenReturn(mockedBorrowOrder);

        //when
        actual = borrowOrderController.createBorrowOrder(mockedCreateBorrowOrderDto);

        //then
        Assert.assertEquals("not OK request", HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    public void createBorrowOrder_when_borrow_order_exist() throws BorrowOrderExistException, UserNotFoundException, BookNotFoundException, BookInsufficientQuantityException {
        //given
        doThrow(BorrowOrderExistException.createWithBookIdAndUserId(mockedId, mockedId)).when(borrowOrderServiceMock).createBorrowOrder(any(Long.class), any(Long.class));

        //when
        actual = borrowOrderController.createBorrowOrder(mockedCreateBorrowOrderDto);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        //checking if ApiError.DUPLICATED_BORROW_ORDER is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.DUPLICATED_BORROW_ORDER.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.DUPLICATED_BORROW_ORDER.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.DUPLICATED_BORROW_ORDER.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }

    @Test
    public void createBorrowOrder_when_user_does_not_exist() throws BorrowOrderExistException, UserNotFoundException, BookNotFoundException, BookInsufficientQuantityException {
        //given
        doThrow(UserNotFoundException.createWithId(mockedId)).when(borrowOrderServiceMock).createBorrowOrder(any(Long.class), any(Long.class));

        //when
        actual = borrowOrderController.createBorrowOrder(mockedCreateBorrowOrderDto);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        //checking if ApiError.USER_DOES_NOT_EXIST is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.USER_DOES_NOT_EXIST.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.USER_DOES_NOT_EXIST.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.USER_DOES_NOT_EXIST.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }

    @Test
    public void createBorrowOrder_when_book_does_not_exist() throws BorrowOrderExistException, UserNotFoundException, BookNotFoundException, BookInsufficientQuantityException {
        //given
        doThrow(BookNotFoundException.createWithId(mockedId)).when(borrowOrderServiceMock).createBorrowOrder(any(Long.class), any(Long.class));

        //when
        actual = borrowOrderController.createBorrowOrder(mockedCreateBorrowOrderDto);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        //checking if ApiError.DUPLICATED_BORROW_ORDER is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.BOOK_DOES_NOT_EXIST.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.BOOK_DOES_NOT_EXIST.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.BOOK_DOES_NOT_EXIST.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }

    @Test
    public void createBorrowOrder_when_book_insufficient_quantity() throws BorrowOrderExistException, UserNotFoundException, BookNotFoundException, BookInsufficientQuantityException {
        //given
        doThrow(BookInsufficientQuantityException.createWithId(mockedId)).when(borrowOrderServiceMock).createBorrowOrder(any(Long.class), any(Long.class));

        //when
        actual = borrowOrderController.createBorrowOrder(mockedCreateBorrowOrderDto);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        //checking if ApiError.BOOK_INSUFFICIENT_QUANTITY is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.BOOK_INSUFFICIENT_QUANTITY.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.BOOK_INSUFFICIENT_QUANTITY.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.BOOK_INSUFFICIENT_QUANTITY.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }

    @Test
    public void deleteBorrowOrder_when_borrow_order_exist() throws BorrowOrderNotFoundException {
        //given
        doNothing().when(borrowOrderServiceMock).deleteBorrowOrder(any(Long.class));

        //when
        voidActual = borrowOrderController.deleteBorrowOrder(mockedId);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.OK, voidActual.getStatusCode());
        Assert.assertNotNull("body is null", voidActual.getBody());
    }

    @Test
    public void deleteBorrowOrder_when_borrow_order_does_not_exist() throws BorrowOrderNotFoundException {
        //given
        doThrow(BorrowOrderNotFoundException.createWithId(mockedId)).when(borrowOrderServiceMock).deleteBorrowOrder(any(Long.class));

        //when
        voidActual = borrowOrderController.deleteBorrowOrder(mockedId);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, voidActual.getStatusCode());
        Assert.assertNotNull("body is null", voidActual.getBody());

        //checking if ApiError.BORROW_ORDER_DOES_NOT_EXIST is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.BORROW_ORDER_DOES_NOT_EXIST.getApiError()), voidActual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.BORROW_ORDER_DOES_NOT_EXIST.getHttpStatus().value()), voidActual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.BORROW_ORDER_DOES_NOT_EXIST.getApiErrorMsg(), voidActual.getBody().getApiErrorMsg());
    }

    @Test
    public void getBorrowOrderById_when_borrow_order_exist() throws BorrowOrderNotFoundException {
        //given
        when(borrowOrderServiceMock.getBorrowOrderById(mockedId)).thenReturn(mockedBorrowOrder);

        //when
        actual = borrowOrderController.getBorrowOrderById(mockedId);

        //then
        Assert.assertEquals("Response not equal", HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    public void getUserById_when_user_does_not_exist() throws BorrowOrderNotFoundException {
        //given
        doThrow(BorrowOrderNotFoundException.createWithId(mockedId)).when(borrowOrderServiceMock).getBorrowOrderById(mockedId);

        //when
        actual = borrowOrderController.getBorrowOrderById(mockedId);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        //checking if ApiError.BORROW_ORDER_DOES_NOT_EXIST is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.BORROW_ORDER_DOES_NOT_EXIST.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.BORROW_ORDER_DOES_NOT_EXIST.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.BORROW_ORDER_DOES_NOT_EXIST.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }

    @Test
    public void getBorrowOrders_with_correct_list() {
        //given
        when(borrowOrderServiceMock.getBorrowOrders()).thenReturn(mockedBorrowOrderDbTable);

        //when
        actualBorrowOrderDbTable = borrowOrderController.getBorrowOrders();

        //then
        Assert.assertEquals("Incorrect array size", mockedBorrowOrderDbTable.size(), actualBorrowOrderDbTable.size());

        //checking if elements in both lists are equal
        for (int i = 0; i < mockedBorrowOrderDbTable.size(); i++) {
            BorrowOrder expectedBorrowOrder = mockedBorrowOrderDbTable.get(i);
            BorrowOrderDto actualBorrowOrder = actualBorrowOrderDbTable.get(i);
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBorrowOrder.getId(), actualBorrowOrder.getId(), i), expectedBorrowOrder.getId(), actualBorrowOrder.getId());
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBorrowOrder.getOrderNumber(), actualBorrowOrder.getOrderNumber(), i), expectedBorrowOrder.getOrderNumber(), actualBorrowOrder.getOrderNumber());
        }
    }

    @Test
    public void getBorrowOrders_with_incorrect_list() {
        //given
        List<BorrowOrder> incorrectBorrowOrderDbTable = new ArrayList<>();
        when(borrowOrderServiceMock.getBorrowOrders()).thenReturn(incorrectBorrowOrderDbTable);

        //when
        actualBorrowOrderDbTable = borrowOrderController.getBorrowOrders();

        //then
        Assert.assertNotEquals("Correct array size", mockedBorrowOrderDbTable.size(), actualBorrowOrderDbTable.size());
    }

    @Test
    public void getBorrowOrdersByUserId_with_correct_list() {
        //given
        when(borrowOrderServiceMock.getBorrowOrdersByUserId(any(Long.class))).thenReturn(mockedBorrowOrderDbTable);

        //when
        actualBorrowOrderDbTable = borrowOrderController.getBorrowOrdersByUserId(mockedId);

        //then
        Assert.assertEquals("Incorrect array size", mockedBorrowOrderDbTable.size(), actualBorrowOrderDbTable.size());

        //checking if elements in both lists are equal
        for (int i = 0; i < mockedBorrowOrderDbTable.size(); i++) {
            BorrowOrder expectedBorrowOrder = mockedBorrowOrderDbTable.get(i);
            BorrowOrderDto actualBorrowOrder = actualBorrowOrderDbTable.get(i);
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBorrowOrder.getId(), actualBorrowOrder.getId(), i), expectedBorrowOrder.getId(), actualBorrowOrder.getId());
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBorrowOrder.getOrderNumber(), actualBorrowOrder.getOrderNumber(), i), expectedBorrowOrder.getOrderNumber(), actualBorrowOrder.getOrderNumber());
        }
    }

    @Test
    public void getBorrowOrdersByUserId_with_incorrect_list() {
        //given
        List<BorrowOrder> incorrectBorrowOrderDbTable = new ArrayList<>();
        when(borrowOrderServiceMock.getBorrowOrdersByUserId(any(Long.class))).thenReturn(incorrectBorrowOrderDbTable);

        //when
        actualBorrowOrderDbTable = borrowOrderController.getBorrowOrdersByUserId(mockedId);

        //then
        Assert.assertNotEquals("Correct array size", mockedBorrowOrderDbTable.size(), actualBorrowOrderDbTable.size());
    }

    @Test
    public void getBorrowOrdersByBookId_with_correct_list() {
        //given
        when(borrowOrderServiceMock.getBorrowOrdersByBookId(any(Long.class))).thenReturn(mockedBorrowOrderDbTable);

        //when
        actualBorrowOrderDbTable = borrowOrderController.getBorrowOrdersByBookId(mockedId);

        //then
        Assert.assertEquals("Incorrect array size", mockedBorrowOrderDbTable.size(), actualBorrowOrderDbTable.size());

        //checking if elements in both lists are equal
        for (int i = 0; i < mockedBorrowOrderDbTable.size(); i++) {
            BorrowOrder expectedBorrowOrder = mockedBorrowOrderDbTable.get(i);
            BorrowOrderDto actualBorrowOrder = actualBorrowOrderDbTable.get(i);
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBorrowOrder.getId(), actualBorrowOrder.getId(), i), expectedBorrowOrder.getId(), actualBorrowOrder.getId());
            Assert.assertEquals(String.format("Element %s is not equal actual %s for i = %d", expectedBorrowOrder.getOrderNumber(), actualBorrowOrder.getOrderNumber(), i), expectedBorrowOrder.getOrderNumber(), actualBorrowOrder.getOrderNumber());
        }
    }

    @Test
    public void getBorrowOrdersByBookId_with_incorrect_list() {
        //given
        List<BorrowOrder> incorrectBorrowOrderDbTable = new ArrayList<>();
        when(borrowOrderServiceMock.getBorrowOrdersByBookId(any(Long.class))).thenReturn(incorrectBorrowOrderDbTable);

        //when
        actualBorrowOrderDbTable = borrowOrderController.getBorrowOrdersByBookId(mockedId);

        //then
        Assert.assertNotEquals("Correct array size", mockedBorrowOrderDbTable.size(), actualBorrowOrderDbTable.size());
    }
}

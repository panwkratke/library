package com.example.library.web.controller;

import com.example.library.core.exception.*;
import com.example.library.core.service.BorrowOrderService;
import com.example.library.model.BorrowOrder;
import com.example.library.web.converter.ApiConverter;
import com.example.library.web.converter.BorrowOrderConverter;
import com.example.library.web.dto.ApiResponseDto;
import com.example.library.web.dto.BorrowOrderDto;
import com.example.library.web.dto.CreateBorrowOrderDto;
import com.example.library.web.dto.enums.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BorrowOrder controller supports all operations regarding http requests for borrow orders. (REST)
 */
@Slf4j
@RestController
@RequestMapping(value = "/orders")
public class BorrowOrderController {

    private final BorrowOrderService borrowOrderService;

    @Autowired
    public BorrowOrderController(BorrowOrderService borrowOrderService) {

        this.borrowOrderService = borrowOrderService;
    }

    /**
     * Processes http post request to add a new borrow order to the repository.
     *
     * @param dto CreateBorrowOrderDto to be processed.
     * @return ResponseEntity containing ApiResponse with created borrow order.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ApiResponseDto<BorrowOrderDto>> createBorrowOrder(@RequestBody CreateBorrowOrderDto dto) {
        BorrowOrder borrowOrder;
        try {
            borrowOrder = this.borrowOrderService.createBorrowOrder(dto.getUserId(), dto.getBookId());
        } catch (BorrowOrderExistException bo) {
            log.error("Borrow order with book id: {} and user id: {} already exist!", dto.getBookId(), dto.getUserId(), bo);
            return ApiConverter.resolveApiError(ApiError.DUPLICATED_BORROW_ORDER);
        } catch (UserNotFoundException u) {
            log.error("User with id: {} does not exist!", dto.getUserId(), u);
            return ApiConverter.resolveApiError(ApiError.USER_DOES_NOT_EXIST);
        } catch (BookNotFoundException b) {
            log.error("Book with id: {} does not exist!", dto.getBookId(), b);
            return ApiConverter.resolveApiError(ApiError.BOOK_DOES_NOT_EXIST);
        } catch (BookInsufficientQuantityException q) {
            log.error("Book with id: {} insufficient quantity in stock!", dto.getBookId(), q);
            return ApiConverter.resolveApiError(ApiError.BOOK_INSUFFICIENT_QUANTITY);
        }
        return BorrowOrderConverter.db2re(borrowOrder);
    }

    /**
     * Processes http delete request to delete a borrow order by borrow order id from the repository.
     *
     * @param id Borrow order id to be searched.
     * @return ResponseEntity containing ApiResponse without a body.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponseDto<Void>> deleteBorrowOrder(@PathVariable Long id) {
        try {
            this.borrowOrderService.deleteBorrowOrder(id);
        } catch (BorrowOrderNotFoundException e) {
            log.error("BorrowOrder with id: {} not found", id, e);
            return ApiConverter.resolveApiError(ApiError.BORROW_ORDER_DOES_NOT_EXIST);
        }
        return ResponseEntity.ok().body(ApiResponseDto.<Void>builder().build());
    }

    /**
     * Processes http get request to retrieve a borrow order by borrow order id from the repository.
     *
     * @param id Borrow order id to be searched.
     * @return ResponseEntity containing ApiResponse with retrieved borrow order.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponseDto<BorrowOrderDto>> getBorrowOrderById(@PathVariable Long id) {
        BorrowOrder borrowOrder;
        try {
            borrowOrder = this.borrowOrderService.getBorrowOrderById(id);
        } catch (BorrowOrderNotFoundException e) {
            log.error("BorrowOrder with id: {} not found", id, e);
            return ApiConverter.resolveApiError(ApiError.BORROW_ORDER_DOES_NOT_EXIST);
        }
        return BorrowOrderConverter.db2re(borrowOrder);
    }

    /**
     * Processes http get request to retrieve a list of all borrow orders from the repository.
     *
     * @return The list of all retrieved borrow orders.
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<BorrowOrderDto> getBorrowOrders() {
        return BorrowOrderConverter.borrowOrderDbListToBorrowOrderDtoList(this.borrowOrderService.getBorrowOrders());
    }

    /**
     * Processes http get request to retrieve a list of all borrow orders by user id from the repository.
     *
     * @param id User id to be searched.
     * @return The list of all retrieved borrow orders by given user id.
     */
    @RequestMapping(value = "/list/user/{id}", method = RequestMethod.GET)
    public List<BorrowOrderDto> getBorrowOrdersByUserId(@PathVariable Long id) {
        return BorrowOrderConverter.borrowOrderDbListToBorrowOrderDtoList(this.borrowOrderService.getBorrowOrdersByUserId(id));
    }

    /**
     * Processes http get request to retrieve a list of all borrow orders by book id from the repository.
     *
     * @param id Book id to be searched.
     * @return The list of all retrieved borrow orders by given book id.
     */
    @RequestMapping(value = "/list/book/{id}", method = RequestMethod.GET)
    public List<BorrowOrderDto> getBorrowOrdersByBookId(@PathVariable Long id) {
        return BorrowOrderConverter.borrowOrderDbListToBorrowOrderDtoList(this.borrowOrderService.getBorrowOrdersByBookId(id));
    }
}

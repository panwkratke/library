package com.example.library.web.converter;

import com.example.library.model.BorrowOrder;
import com.example.library.web.dto.ApiResponseDto;
import com.example.library.web.dto.BorrowOrderDto;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Borrow order converter supports all required operations regarding conversions between borrow order objects.
 */
public class BorrowOrderConverter {

    /**
     * Converts borrow order db to borrow order dto.
     *
     * @param db Borrow order db to be converted.
     * @return BorrowOrderDto after conversion.
     */
    public static BorrowOrderDto db2dto(BorrowOrder db) {
        BorrowOrderDto dto = new BorrowOrderDto();
        BorrowOrderConverter.db2dto(dto, db);
        return dto;
    }

    private static void db2dto(BorrowOrderDto dto, BorrowOrder db) {
        dto.setId(db.getId());
        dto.setUser(UserConverter.db2dto(db.getUser()));
        dto.setBook(BookConverter.db2dto(db.getBook()));
        dto.setOrderNumber(db.getOrderNumber());
        dto.setCreationDate(db.getCreationDate().getTime().toString());
    }

    /**
     * Converts a list of borrow orders db to list of borrow orders dto.
     *
     * @param dbBorrowOrderList List of borrow orders db to be converted.
     * @return List of borrow orders dto after conversion.
     */
    public static List<BorrowOrderDto> borrowOrderDbListToBorrowOrderDtoList(List<BorrowOrder> dbBorrowOrderList) {
        List<BorrowOrderDto> borrowOrderDtoList = new ArrayList<>();
        for (BorrowOrder bo : dbBorrowOrderList) {
            borrowOrderDtoList.add(db2dto(bo));
        }
        return borrowOrderDtoList;
    }

    /**
     * Converts borrow order db to ResponseEntity.
     *
     * @param db Borrow order db to be converted.
     * @return ResponseEntity containing ApiResponse with converted BorrowOrder to BorrowOrderDto.
     */
    public static ResponseEntity<ApiResponseDto<BorrowOrderDto>> db2re(BorrowOrder db) {
        BorrowOrderDto dto = BorrowOrderConverter.db2dto(db);
        return ResponseEntity.ok().body(ApiResponseDto.<BorrowOrderDto>builder().content(dto).build());
    }
}

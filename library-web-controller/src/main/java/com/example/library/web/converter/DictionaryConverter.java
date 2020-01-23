package com.example.library.web.converter;

import com.example.library.model.Dictionary;
import com.example.library.web.dto.ApiResponseDto;
import com.example.library.web.dto.DictionaryDto;
import org.springframework.http.ResponseEntity;

/**
 * Dictionary converter supports all required operations regarding conversions between dictionary objects.
 */
public class DictionaryConverter {

    /**
     * Converts dictionary db to dictionary dto.
     *
     * @param db Dictionary db to be converted.
     * @return DictionaryDto after conversion.
     */
    public static DictionaryDto db2dto(Dictionary db) {
        DictionaryDto dto = new DictionaryDto();
        DictionaryConverter.db2dto(dto, db);
        return dto;
    }

    private static void db2dto(DictionaryDto dto, Dictionary db) {
        dto.setDictionaryName(db.getDictionaryName());
        dto.setWords(db.getWords());
    }

    /**
     * Converts dictionary db to ResponseEntity.
     *
     * @param db Dictionary db to be converted.
     * @return ResponseEntity containing ApiResponse with converted Dictionary to DictionaryDto.
     */
    public static ResponseEntity<ApiResponseDto<DictionaryDto>> db2re(Dictionary db) {
        DictionaryDto dto = DictionaryConverter.db2dto(db);
        return ResponseEntity.ok().body(ApiResponseDto.<DictionaryDto>builder().content(dto).build());
    }
}

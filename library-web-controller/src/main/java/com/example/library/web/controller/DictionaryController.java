package com.example.library.web.controller;

import com.example.library.core.exception.DictionaryNotFoundException;
import com.example.library.core.service.DictionaryService;
import com.example.library.model.Dictionary;
import com.example.library.web.converter.ApiConverter;
import com.example.library.web.converter.DictionaryConverter;
import com.example.library.web.dto.ApiResponseDto;
import com.example.library.web.dto.DictionaryDto;
import com.example.library.web.dto.enums.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dictionary controller supports all controller operations regarding http requests for dictionaries. (REST)
 */
@Slf4j
@RestController
@RequestMapping("/dictionaries")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    /**
     * Processes http get request to retrieve a dictionary by dictionary name from the repository.
     *
     * @param dictionaryName Dictionary name to be searched.
     * @return ResponseEntity containing ApiResponse with retrieved dictionary.
     */
    @RequestMapping(value = "/{dictionaryName}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponseDto<DictionaryDto>> getDictionaryByDictionaryName(@PathVariable String dictionaryName) {
        Dictionary dictionary;
        try {
            dictionary = this.dictionaryService.getDictionaryByDictionaryName(dictionaryName);
        } catch (DictionaryNotFoundException e) {
            log.error("Dictionary with dictionary name: {} not found", dictionaryName, e);
            return ApiConverter.resolveApiError(ApiError.DICTIONARY_DOES_NOT_EXIST);
        }
        return DictionaryConverter.db2re(dictionary);
    }
}

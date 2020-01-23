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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest({DictionaryConverter.class, ApiConverter.class})
public class DictionaryControllerTest {

    private String mockedDictionaryName;
    private Dictionary mockedDictionary;
    private DictionaryService dictionaryServiceMock;
    private ResponseEntity<ApiResponseDto<DictionaryDto>> actual;
    private DictionaryController dictionaryController;

    @Before
    public void initialize() {
        mockedDictionaryName = "mockedDictionaryName";
        DictionaryDto mockedDictionaryDto = new DictionaryDto();
        mockedDictionaryDto.setDictionaryName(mockedDictionaryName);
        mockedDictionary = new Dictionary();
        mockedDictionary.setDictionaryName(mockedDictionaryName);
        dictionaryServiceMock = mock(DictionaryService.class);
        dictionaryController = new DictionaryController(dictionaryServiceMock);
        ResponseEntity<ApiResponseDto<DictionaryDto>> mockedResponseEntity = new ResponseEntity<>(HttpStatus.OK);
        PowerMockito.mockStatic(DictionaryConverter.class);
        BDDMockito.given(DictionaryConverter.db2dto(any(Dictionary.class))).willReturn(mockedDictionaryDto);
        BDDMockito.given(DictionaryConverter.db2re(any(Dictionary.class))).willReturn(mockedResponseEntity);
    }

    @Test
    public void getDictionaryByDictionaryName_when_dictionary_exist() throws DictionaryNotFoundException {
        //give
        when(dictionaryServiceMock.getDictionaryByDictionaryName(mockedDictionaryName)).thenReturn(mockedDictionary);

        //when
        actual = dictionaryController.getDictionaryByDictionaryName(mockedDictionaryName);

        //then
        Assert.assertEquals("Response not equal", HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    public void getDictionaryByDictionaryName_when_dictionary_does_not_exist() throws DictionaryNotFoundException {
        //give
        doThrow(DictionaryNotFoundException.createWithDictionaryName(mockedDictionaryName)).when(dictionaryServiceMock).getDictionaryByDictionaryName(mockedDictionaryName);

        //when
        actual = dictionaryController.getDictionaryByDictionaryName(mockedDictionaryName);

        //then
        Assert.assertEquals("not BAD_REQUEST", HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assert.assertNotNull("body is null", actual.getBody());

        //checking if ApiError.DICTIONARY_DOES_NOT_EXIST is returned in actual.getBody()
        Assert.assertEquals("wrong api error", Integer.valueOf(ApiError.DICTIONARY_DOES_NOT_EXIST.getApiError()), actual.getBody().getApiError());
        Assert.assertEquals("wrong http status", Integer.valueOf(ApiError.DICTIONARY_DOES_NOT_EXIST.getHttpStatus().value()), actual.getBody().getHttpError());
        Assert.assertEquals("wrong api error msg", ApiError.DICTIONARY_DOES_NOT_EXIST.getApiErrorMsg(), actual.getBody().getApiErrorMsg());
    }

}

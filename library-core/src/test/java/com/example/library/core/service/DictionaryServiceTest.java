package com.example.library.core.service;

import com.example.library.core.exception.DictionaryNotFoundException;
import com.example.library.core.repository.DictionaryRepository;
import com.example.library.model.Dictionary;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class DictionaryServiceTest {

    private String mockedDictionaryName;
    private String mockedDictionaryNameDoesNotExist;
    private Dictionary actualDictionary;
    private Optional<Dictionary> mockedDictionaryOptional;
    private DictionaryRepository dictionaryRepositoryMock;
    private DictionaryService dictionaryService;

    @Before
    public void initialize() {
        mockedDictionaryName = "mockedDictionaryName";
        mockedDictionaryNameDoesNotExist = "mockedDictionaryNameDoesNotExist";
        actualDictionary = new Dictionary();
        Dictionary mockedDictionary = new Dictionary();
        mockedDictionary.setDictionaryName(mockedDictionaryName);
        mockedDictionaryOptional = Optional.of(mockedDictionary);
        dictionaryRepositoryMock = mock(DictionaryRepository.class);
        dictionaryService = new DictionaryService(dictionaryRepositoryMock);
    }

    @Test
    public void getDictionaryByDictionaryName_when_dictionary_exist() {
        //given
        when(dictionaryRepositoryMock.findByDictionaryName(mockedDictionaryName)).thenReturn(mockedDictionaryOptional);

        //when
        try {
            actualDictionary = dictionaryService.getDictionaryByDictionaryName(mockedDictionaryName);
        } catch (DictionaryNotFoundException e) {
            log.error("Dictionary not found", e);
            Assert.fail("Dictionary not found");
            return;
        }

        //then
        Assert.assertEquals("Dictionary name is incorrect", mockedDictionaryName, actualDictionary.getDictionaryName());
    }

    @Test(expected = DictionaryNotFoundException.class)
    public void getDictionaryByDictionaryName_when_dictionary_does_not_exist() throws DictionaryNotFoundException {
        //given
        when(dictionaryRepositoryMock.findByDictionaryName(mockedDictionaryName)).thenReturn(mockedDictionaryOptional);

        //when then
        dictionaryService.getDictionaryByDictionaryName(mockedDictionaryNameDoesNotExist);
    }
}

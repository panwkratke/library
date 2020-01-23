package com.example.library.core.service;

import com.example.library.core.exception.DictionaryNotFoundException;
import com.example.library.core.repository.DictionaryRepository;
import com.example.library.model.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Dictionary service retrieves dictionaries from repository.
 */
@Service
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    @Autowired
    public DictionaryService(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    /**
     * Retrieves the dictionary from the repository by dictionary name.
     *
     * @param dictionaryName Dictionary name to be searched in the repository.
     * @return Dictionary from the dictionary repository with the given dictionary name.
     * @throws DictionaryNotFoundException The dictionary with the given dictionary name does not exist in the repository.
     */
    public Dictionary getDictionaryByDictionaryName(String dictionaryName) throws DictionaryNotFoundException {
        Optional<Dictionary> dictionaryOptional = this.dictionaryRepository.findByDictionaryName(dictionaryName);
        if (dictionaryOptional.isEmpty()) {
            throw DictionaryNotFoundException.createWithDictionaryName(dictionaryName);
        }
        return dictionaryOptional.get();
    }
}

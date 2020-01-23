package com.example.library.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DictionaryDto {

    private String dictionaryName;
    private List<String> words;
}

package com.example.library.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Dictionary extends AbstractEntity {

    private String dictionaryName;

    @ElementCollection
    private List<String> words;
}

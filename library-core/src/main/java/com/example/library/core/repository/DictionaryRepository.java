package com.example.library.core.repository;

import com.example.library.model.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

    @Query("SELECT d FROM Dictionary d WHERE lower(d.dictionaryName) = lower(:dictionaryName)")
    Optional<Dictionary> findByDictionaryName(@Param("dictionaryName") String dictionaryName);
}

package com.example.library.core.repository;

import com.example.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    @Query("SELECT b FROM Book b WHERE lower(b.title) = lower(:title) AND lower(b.author) = lower(:author)")
    Optional<Book> findByTitleAndAuthor(@Param("title") String title, @Param("author") String author);

    @Query("SELECT b FROM Book b WHERE lower(b.author) LIKE lower(concat('%', :author,'%')) ORDER BY b.title")
    List<Book> findAllByAuthor(@Param("author") String author);

    @Query("SELECT b FROM Book b WHERE lower(b.genre) = lower(:genre) ORDER BY b.title")
    List<Book> findAllByGenre(@Param("genre") String genre);

    @Query("SELECT b FROM Book b WHERE lower(b.title) LIKE lower(concat('%', :title,'%')) ORDER BY b.title")
    List<Book> findAllByTitle(@Param("title") String title);

    @Query("SELECT b FROM Book b ORDER BY b.title")
    List<Book> findAllBooks();
}

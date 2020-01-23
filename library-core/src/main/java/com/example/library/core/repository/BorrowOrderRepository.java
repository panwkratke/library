package com.example.library.core.repository;

import com.example.library.model.BorrowOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowOrderRepository extends JpaRepository<BorrowOrder, Long> {

    @Query("SELECT bo FROM BorrowOrder bo WHERE bo.user.id = :id")
    List<BorrowOrder> findAllByUserId(@Param("id") Long userId);

    @Query("SELECT bo FROM BorrowOrder bo WHERE bo.book.id = :id")
    List<BorrowOrder> findAllByBookId(@Param("id") Long id);

    @Query("SELECT bo FROM BorrowOrder bo WHERE bo.book.id = :bookId AND bo.user.id = :userId")
    Optional<BorrowOrder> findByBookIdAndUserId(@Param("bookId") Long bookId, @Param("userId") Long userId);
}

package com.kin.jjandolnet.api.domain.expense.repository;

import com.kin.jjandolnet.api.domain.expense.entity.Expense;
import com.kin.jjandolnet.api.domain.expense.entity.ExpenseCategory;
import com.kin.jjandolnet.api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT e FROM Expense e " +
            "WHERE e.user.id = :userId " +
            "AND CAST(e.expenseDate AS string) LIKE CONCAT(:expenseDate, '%') " +
            "ORDER BY e.expenseDate DESC")
    List<Expense> findAllBy(@Param("userId") Long userId,
                            @Param("expenseDate") String expenseDate);
    Optional<Expense> findByIdAndUser(Long id, User user);
    void deleteByIdAndUserId(Long id, Long userId);
}

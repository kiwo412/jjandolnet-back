package com.kin.jjandolnet.api.domain.expense.dto;

import com.kin.jjandolnet.api.domain.expense.entity.Expense;
import com.kin.jjandolnet.api.domain.expense.entity.ExpenseCategory;
import com.kin.jjandolnet.api.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseDto {

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private Long amount;
        private String memo;
        private LocalDate expenseDate;
        private String categoryName;
        private LocalDateTime createdAt;

        public static Response from(Expense expense) {
            return Response.builder()
                    .id(expense.getId())
                    .amount(expense.getAmount())
                    .memo(expense.getMemo())
                    .expenseDate(expense.getExpenseDate())
                    .categoryName(expense.getCategory().getName())
                    .createdAt(expense.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CreateRequest {
        private Long amount;
        private String memo;
        private LocalDate expenseDate;
        private Long categoryId;

        public Expense toEntity(User user, ExpenseCategory category) {
            return Expense.builder()
                    .amount(amount)
                    .memo(memo)
                    .expenseDate(expenseDate)
                    .user(user)
                    .category(category)
                    .build();
        }
    }
}

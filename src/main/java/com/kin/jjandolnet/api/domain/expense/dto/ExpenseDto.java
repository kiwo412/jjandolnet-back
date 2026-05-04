package com.kin.jjandolnet.api.domain.expense.dto;

import com.kin.jjandolnet.api.domain.expense.entity.Expense;
import com.kin.jjandolnet.api.domain.expense.entity.ExpenseCategory;
import com.kin.jjandolnet.api.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
        private CategoryDto.Response category;
        private LocalDateTime createdAt;

        public static Response from(Expense expense) {
            return Response.builder()
                    .id(expense.getId())
                    .amount(expense.getAmount())
                    .memo(expense.getMemo())
                    .expenseDate(expense.getExpenseDate())
                    .category(CategoryDto.Response.from(expense.getCategory()))
                    .createdAt(expense.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CreateRequest {

        @NotNull(message = "금액은 필수입니다.")
        @Positive(message = "금액은 0원보다 커야 합니다.")
        private Long amount;
        private String memo;
        @NotNull(message = "날짜는 필수 입니다.")
        private LocalDate expenseDate;
        @NotNull(message = "카테고리는 필수입니다.")
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

    @Getter
    @Builder
    public static class UpdateRequest {

        @NotNull(message = "ID는 필수입니다.")
        private Long id;
        @NotNull(message = "금액은 필수입니다.")
        @Positive(message = "금액은 0원보다 커야 합니다.")
        private Long amount;
        private String memo;
        @NotNull(message = "날짜는 필수 입니다.")
        private LocalDate expenseDate;
        @NotNull(message = "카테고리는 필수입니다.")
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

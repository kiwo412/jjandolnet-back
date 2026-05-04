package com.kin.jjandolnet.api.domain.expense.dto;

import com.kin.jjandolnet.api.domain.expense.entity.Income;
import com.kin.jjandolnet.api.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class IncomeDto {

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private Long amount;
        private String incomeDate;

        public static Response from(Income income) {
            return Response.builder()
                    .id(income.getId())
                    .amount(income.getAmount())
                    .incomeDate(income.getIncomeDate())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CuRequest {
        @NotNull(message = "금액은 필수입니다.")
        @Positive(message = "금액은 0원보다 커야 합니다.")
        private Long amount;
        @NotBlank(message = "잘못된 날짜 값입니다.")
        private String incomeDate;

        public Income toEntity(User user) {
            return Income.builder()
                    .amount(amount)
                    .incomeDate(incomeDate)
                    .user(user)
                    .build();
        }
    }
}

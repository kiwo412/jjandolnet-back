package com.kin.jjandolnet.api.domain.expense.dto;

import com.kin.jjandolnet.api.domain.expense.entity.Income;
import com.kin.jjandolnet.api.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

public class ScoreDto {

    @Getter
    @Builder
    public static class Response {
        private Long score;
        private Long totalExpense;
        private String feedback;
        private boolean status;

        public static Response from(Long score, Long totalExpense, String feedback, boolean status) {
            return Response.builder()
                    .score(score)
                    .totalExpense(totalExpense)
                    .feedback(feedback)
                    .status(status)
                    .build();
        }
    }
}

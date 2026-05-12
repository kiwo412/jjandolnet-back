package com.kin.jjandolnet.api.domain.expense.dto;

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

package com.kin.jjandolnet.api.domain.expense.dto;

import lombok.*;

import java.util.List;

public class MyCategoryDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private List<CategoryInfo> categories;
        private boolean status;

        public static Response of(List<CategoryInfo> categories, boolean status) {
            return Response.builder()
                    .categories(categories)
                    .status(status)
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInfo {
        private String name;
        private Long expense;
        private Double percent;

        // QueryDSL Projections.constructor를 위한 2개짜리 생성자
        public CategoryInfo(String name, Long expense) {
            this.name = name;
            this.expense = expense;
        }

    }



}
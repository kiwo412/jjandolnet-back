package com.kin.jjandolnet.api.domain.expense.dto;

import com.kin.jjandolnet.api.domain.expense.entity.ExpenseCategory;
import lombok.Builder;
import lombok.Getter;

public class CategoryDto {

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private String icon;
        private String color;

        public static Response from(ExpenseCategory category) {
            return Response.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .icon(category.getIcon())
                    .color(category.getColor())
                    .build();
        }
    }
}

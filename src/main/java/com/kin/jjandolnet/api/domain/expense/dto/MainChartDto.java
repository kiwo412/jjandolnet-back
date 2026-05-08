package com.kin.jjandolnet.api.domain.expense.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MainChartDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private List<MainChartDto.MainChartInfo> mainChartValues;

        public static Response of(List<MainChartDto.MainChartInfo> chartData) {
            return Response.builder()
                    .mainChartValues(chartData)
                    .build();
        }

    }

    @Getter
    @Builder
    @NoArgsConstructor
    public static class MainChartInfo {
        private String category;
        private Long average;

        public MainChartInfo(String category, Double average) {
            this.category = category;
            this.average = (average != null) ? average.longValue() : 0L;
        }

        private MainChartInfo(String category, Long average) {
            this.category = category;
            this.average = average;
        }

    }

    @Getter
    @Builder
    public static class searchCondition {
        @NotBlank(message = "검색조건을 선택 해야합니다.")
        private String filter;
        @NotNull(message = "검색조건을 선택 해야합니다.")
        private Long selectedCategory;

    }
}

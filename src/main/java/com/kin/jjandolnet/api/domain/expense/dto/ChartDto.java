package com.kin.jjandolnet.api.domain.expense.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ChartDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MainResponse {
        private List<ChartDto.MainChartInfo> mainChartValues;

        public static MainResponse of(List<ChartDto.MainChartInfo> chartData) {
            return MainResponse.builder()
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
    @AllArgsConstructor
    public static class SubResponse {
        private String category;
        private String message;
        private double percent;
        private double categoryAverage;
        private Long myTotal;

        public static SubResponse from(
                String category, String message, double percent, double categoryAverage,Long myTotal) {
            return SubResponse.builder()
                    .category(category)
                    .message(message)
                    .percent(percent)
                    .categoryAverage(categoryAverage)
                    .myTotal(myTotal)
                    .build();
        }

    }

    @Getter
    @Builder
    public static class searchCondition {
        @Schema(example = "age")
        @NotBlank(message = "검색조건을 선택 해야합니다.")
        private String filter;
        @Schema(example = "0")
        @NotNull(message = "검색조건을 선택 해야합니다.")
        private Long selectedCategory;

    }
}

package com.kin.jjandolnet.api.domain.expense.repository;

import com.kin.jjandolnet.api.domain.expense.dto.ChartDto;
import com.kin.jjandolnet.api.domain.expense.dto.MyCategoryDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepositoryCustom {
    Optional<Long> sumAmountByUserIdAndMonth(Long userId, LocalDate date, Long categoryId);
    List<MyCategoryDto.CategoryInfo> getCategorySumByUserIdAndMonth(Long userId, LocalDate date);
    List<ChartDto.MainChartInfo> findAverageByCondition(ChartDto.searchCondition searchCondition, LocalDate date);
    String findUserGroupValue(Long userId, String filter);
    Double findGroupAverage(String filter, String groupValue, ChartDto.searchCondition condition, LocalDate date);
}

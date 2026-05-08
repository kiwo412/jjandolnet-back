package com.kin.jjandolnet.api.domain.expense.repository;

import com.kin.jjandolnet.api.domain.expense.dto.MyCategoryDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepositoryCustom {
    Optional<Long> sumAmountByUserIdAndMonth(Long userId, LocalDate date);
    List<MyCategoryDto.CategoryInfo> getCategorySumByUserIdAndMonth(Long userId, LocalDate date);
}

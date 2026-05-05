package com.kin.jjandolnet.api.domain.expense.repository;

import java.time.LocalDate;
import java.util.Optional;

public interface ExpenseRepositoryCustom {
    Optional<Long> sumAmountByUserIdAndMonth(Long userId, LocalDate date);
}

package com.kin.jjandolnet.api.domain.expense.repository;

import com.kin.jjandolnet.api.domain.expense.entity.QExpense;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public class ExpenseRepositoryImpl implements ExpenseRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> sumAmountByUserIdAndMonth(Long userId, LocalDate date) {
        QExpense expense = QExpense.expense;

        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());

        Long result = queryFactory
                .select(expense.amount.sum())
                .from(expense)
                .where(
                        expense.user.id.eq(userId),
                        expense.expenseDate.between(start, end)
                )
                .fetchOne();


        return Optional.ofNullable(result);
    }
}

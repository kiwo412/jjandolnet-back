package com.kin.jjandolnet.api.domain.expense.repository;

import com.kin.jjandolnet.api.domain.expense.dto.MyCategoryDto;
import com.kin.jjandolnet.api.domain.expense.entity.QExpense;
import com.kin.jjandolnet.api.domain.expense.entity.QExpenseCategory;
import com.kin.jjandolnet.api.domain.user.entity.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.kin.jjandolnet.api.domain.expense.entity.QExpense.expense;
import static com.kin.jjandolnet.api.domain.expense.entity.QExpenseCategory.expenseCategory;

@RequiredArgsConstructor
public class ExpenseRepositoryImpl implements ExpenseRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> sumAmountByUserIdAndMonth(Long userId, LocalDate date) {

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

    @Override
    public List<MyCategoryDto.CategoryInfo> getCategorySumByUserIdAndMonth(Long userId, LocalDate date) {

        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());

        return queryFactory
                .select(Projections.constructor(MyCategoryDto.CategoryInfo.class,
                        expenseCategory.name,
                        expense.amount.sum()
                ))
                .from(expense)
                .join(expense.category, expenseCategory)
                .where(expense.user.id.eq(userId),
                        expense.expenseDate.between(start, end))
                .groupBy(expenseCategory.id, expenseCategory.name)
                .orderBy(expense.amount.sum().desc())
                .fetch();
    }

}

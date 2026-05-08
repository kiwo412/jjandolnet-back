package com.kin.jjandolnet.api.domain.expense.repository;

import com.kin.jjandolnet.api.domain.expense.dto.MainChartDto;
import com.kin.jjandolnet.api.domain.expense.dto.MyCategoryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import static com.kin.jjandolnet.api.domain.expense.entity.QExpense.expense;
import static com.kin.jjandolnet.api.domain.expense.entity.QExpenseCategory.expenseCategory;
import static com.kin.jjandolnet.api.domain.user.entity.QAddress.address;
import static com.kin.jjandolnet.api.domain.user.entity.QJob.job;
import static com.kin.jjandolnet.api.domain.user.entity.QUser.user;

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

    @Override
    public List<MainChartDto.MainChartInfo> findAverageByCondition(MainChartDto.searchCondition searchCondition, LocalDate date) {
        StringExpression groupByExpr = getGroupByExpression(searchCondition.getFilter());

        JPAQuery<MainChartDto.MainChartInfo> query = queryFactory
                .select(Projections.constructor(MainChartDto.MainChartInfo.class,
                        groupByExpr,
                        expense.amount.sum().doubleValue()
                                .divide(user.id.countDistinct())
                ))
                .from(expense)
                .join(expense.user, user);

       if ("job".equals(searchCondition.getFilter())) {
            query.join(user.job, job);
        } else if ("addr".equals(searchCondition.getFilter())) {
            query.join(user.address, address);
        }

        return query.where(
                        monthEq(date),
                        categoryEq(searchCondition.getSelectedCategory())
                )
                .groupBy(groupByExpr)
                .fetch();
    }

    private StringExpression getGroupByExpression(String filter) {
        if ("job".equals(filter)) {
            return user.job.name;
        } else if("addr".equals(filter)) {
            return user.address.name;
        }else {
            int currentYear = LocalDate.now().getYear();

            return Expressions.stringTemplate(
                    "CASE " +
                            "  WHEN (YEAR(CURRENT_DATE) - YEAR({0})) >= 60 THEN '60대 이상' " +
                            "  ELSE CONCAT(FLOOR(ABS(YEAR({0}) - " + currentYear + ") / 10) * 10, '대') " +
                            "END",
                    user.birthDate
            );
        }
    }

    private BooleanExpression categoryEq(Long categoryId) {
        return (categoryId == null || categoryId == 0L) ? null : expense.category.id.eq(categoryId);
    }

    private BooleanExpression monthEq(LocalDate date) {

        if (date == null) return null;

        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.with(TemporalAdjusters.lastDayOfMonth());

        return expense.expenseDate.between(start, end);
    }

}

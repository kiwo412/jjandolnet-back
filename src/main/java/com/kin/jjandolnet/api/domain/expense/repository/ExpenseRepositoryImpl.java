package com.kin.jjandolnet.api.domain.expense.repository;

import com.kin.jjandolnet.api.domain.expense.dto.ChartDto;
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
    public Optional<Long> sumAmountByUserIdAndMonth(Long userId, LocalDate date, Long categoryId) {

        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());

        Long result = queryFactory
                .select(expense.amount.sum())
                .from(expense)
                .where(
                        expense.user.id.eq(userId),
                        expense.expenseDate.between(start, end),
                        categoryEq(categoryId)
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
    public List<ChartDto.MainChartInfo> findAverageByCondition(ChartDto.searchCondition searchCondition, LocalDate date) {
        StringExpression groupByExpr = getGroupByExpression(searchCondition.getFilter());
        NumberExpression<Double> avgAmount = expense.amount.sum().doubleValue()
                .divide(user.id.countDistinct());

        NumberExpression<Double> roundedAvg = Expressions.numberTemplate(Double.class,
                "ROUND({0}, 0)", avgAmount);

        JPAQuery<ChartDto.MainChartInfo> query = queryFactory
                .select(Projections.constructor(ChartDto.MainChartInfo.class,
                        groupByExpr,
                        roundedAvg
                ))
                .from(expense)
                .join(expense.user, user);

        if ("job".equals(searchCondition.getFilter())) query.join(user.job, job);
        else if ("addr".equals(searchCondition.getFilter())) query.join(user.address, address);

        return query.where(
                        monthEq(date),
                        categoryEq(searchCondition.getSelectedCategory())
                )
                .groupBy(groupByExpr)
                .fetch();
    }

    @Override
    public String findUserGroupValue(Long userId, String filter) {
        StringExpression groupByExpr = getGroupByExpression(filter);

        JPAQuery<String> query = queryFactory
                .select(groupByExpr)
                .from(user);

        if ("job".equals(filter)) query.join(user.job, job);
        else if ("addr".equals(filter)) query.join(user.address, address);

        return query.where(user.id.eq(userId))
                .fetchOne();
    }

    @Override
    public Double findGroupAverage(String filter, String groupValue, ChartDto.searchCondition condition, LocalDate date) {
        StringExpression groupByExpr = getGroupByExpression(filter);

        NumberExpression<Double> avgExpression = expense.amount.sum().coalesce(0L).doubleValue()
                .divide(user.id.countDistinct());

        NumberExpression<Double> roundedAvg = Expressions.numberTemplate(Double.class, "ROUND({0}, 0)", avgExpression);

        JPAQuery<Double> query = queryFactory
                .select(roundedAvg)
                .from(expense)
                .join(expense.user, user);


        if ("job".equals(filter)) query.join(user.job, job);
        else if ("addr".equals(filter)) query.join(user.address, address);

        return query.where(
                        monthEq(date),
                        categoryEq(condition.getSelectedCategory()),
                        groupByExpr.eq(groupValue)
                )
                .fetchOne();
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

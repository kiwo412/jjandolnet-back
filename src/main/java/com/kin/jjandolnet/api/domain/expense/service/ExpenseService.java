package com.kin.jjandolnet.api.domain.expense.service;

import com.kin.jjandolnet.api.domain.expense.constant.ExpenseScoreLevel;
import com.kin.jjandolnet.api.domain.expense.constant.SubChart1Level;
import com.kin.jjandolnet.api.domain.expense.dto.*;
import com.kin.jjandolnet.api.domain.expense.entity.Expense;
import com.kin.jjandolnet.api.domain.expense.entity.ExpenseCategory;
import com.kin.jjandolnet.api.domain.expense.entity.Income;
import com.kin.jjandolnet.api.domain.expense.repository.CategoryRepository;
import com.kin.jjandolnet.api.domain.expense.repository.ExpenseRepository;
import com.kin.jjandolnet.api.domain.expense.repository.IncomeRepository;
import com.kin.jjandolnet.api.domain.user.entity.User;
import com.kin.jjandolnet.api.domain.user.repository.UserRepository;
import com.kin.jjandolnet.global.error.exception.BusinessException;
import com.kin.jjandolnet.global.error.exception.ErrorCode;
import com.kin.jjandolnet.global.util.ValidateDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    private final ValidateDate validateDate;

    @Transactional(readOnly = true)
    public List<CategoryDto.Response> getCategoryList() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDto.Response::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExpenseDto.Response> getExpenseList(Long userId, String expenseDate) {
        return expenseRepository.findAllBy(userId, expenseDate)
                .stream()
                .map(ExpenseDto.Response::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public IncomeDto.Response getIncome(Long userId, String incomeDate) {

        LocalDate dateToCheck = YearMonth.parse(incomeDate).atDay(1);
        validateDate.validateNotFutureAndServiceStartDate(dateToCheck);

        return incomeRepository.findByUserIdAndIncomeDate(userId, incomeDate)
                .map(IncomeDto.Response::from)
                .orElseGet(() -> IncomeDto.Response.builder().build());
    }

    @Transactional(readOnly = true)
    public ScoreDto.Response getMyScore(Long userId, String scoreDate) {

        LocalDate dateToCheck = YearMonth.parse(scoreDate).atDay(1);
        validateDate.validateNotFutureAndServiceStartDate(dateToCheck);

        //수입
        Long income = incomeRepository.findByUserIdAndIncomeDate(userId, scoreDate)
                .map(Income::getAmount)
                .orElse(0L);

        //월 소비 합계
        Long totalExpense = expenseRepository.sumAmountByUserIdAndMonth(userId, dateToCheck, null)
                .orElse(0L);

        if(income == 0L || totalExpense == 0L){
            return ScoreDto.Response.from(0L, 0L,
                    "이번 달 수입/지출을 입력하고 짠돌력을 확인해보세요!", false);
        }

        Long rawScore = 100 - Math.round((double) totalExpense / income * 100);
        Long score = Math.max(-100L, rawScore);
        String message = ExpenseScoreLevel.findByScore(score).getMessage();

        return ScoreDto.Response.from(score, totalExpense, message, true);
    }

    @Transactional(readOnly = true)
    public MyCategoryDto.Response getMyCategory(Long userId, String categoryDate) {

        LocalDate dateToCheck = YearMonth.parse(categoryDate).atDay(1);
        validateDate.validateNotFutureAndServiceStartDate(dateToCheck);

        Long income = incomeRepository.findByUserIdAndIncomeDate(userId, categoryDate)
                .map(Income::getAmount)
                .orElse(0L);

        Long totalExpense = expenseRepository.sumAmountByUserIdAndMonth(userId, dateToCheck, null)
                .orElse(0L);

        if(income == 0L || totalExpense == 0L){
            return MyCategoryDto.Response.of(null, false);
        }

        List<MyCategoryDto.CategoryInfo> categoryInfos =
                expenseRepository.getCategorySumByUserIdAndMonth(userId, dateToCheck);

        categoryInfos.forEach(info -> {
            double percentage = (double) info.getExpense() / totalExpense * 100;

            info.setPercent(Math.round(percentage * 10) / 10.0);
        });

        return MyCategoryDto.Response.of(categoryInfos, true);
    }

    @Transactional(readOnly = true)
    public ChartDto.MainResponse getMainChart(ChartDto.searchCondition searchCondition) {

        LocalDate now = LocalDate.now();

        List<ChartDto.MainChartInfo> chartInfos =
                expenseRepository.findAverageByCondition(searchCondition, now);

        return ChartDto.MainResponse.of(chartInfos);
    }

    @Transactional(readOnly = true)
    public ChartDto.SubResponse getSubChart1(Long userId, ChartDto.searchCondition searchCondition) {

        double percent = 0;
        String message;

        LocalDate now = LocalDate.now();

        String filter = searchCondition.getFilter();
        Long selectedCategory = searchCondition.getSelectedCategory();

        Long myTotal = expenseRepository.sumAmountByUserIdAndMonth(userId, now, selectedCategory).orElse(0L);

        String myGroupValue = expenseRepository.findUserGroupValue(userId, filter);
        Double groupAverage = expenseRepository.findGroupAverage(filter, myGroupValue, searchCondition, now);

        if(myTotal == 0L || groupAverage == null){
            return ChartDto.SubResponse.from(null,
                    "이번 달 소비 내역이 없어 비교가 어려워요. 내 소비 내역을 먼저 등록해보는 건 어떨까요?",
                    0.0, 0, 0L);
        }

        percent = (myTotal / groupAverage) * 100;
        percent = Math.round(percent * 10.0) / 10.0;

        SubChart1Level status = SubChart1Level.of(percent);
        message = status.formatMessage(myGroupValue, percent);

        return ChartDto.SubResponse.from(myGroupValue, message, percent, groupAverage, myTotal);
    }

    @Transactional
    public void cuIncome(IncomeDto.CuRequest request, Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        incomeRepository.findByUserIdAndIncomeDate(userId, request.getIncomeDate())
                .ifPresentOrElse(
                // 1건 존재하면 값만 변경
                income -> income.update(request.getAmount(), request.getIncomeDate()),
                // 없으면 새로 생성해서 저장
                () -> incomeRepository.save(Income.builder()
                        .amount(request.getAmount())
                        .incomeDate(request.getIncomeDate())
                        .user(user)
                        .build())
        );
    }

    @Transactional
    public void createExpense(ExpenseDto.CreateRequest request, Long id){

        validateDate.validateNotFutureAndServiceStartDate(request.getExpenseDate());

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        ExpenseCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        Expense expense = Expense.builder()
                .user(user)
                .amount(request.getAmount())
                .expenseDate(request.getExpenseDate())
                .memo(request.getMemo())
                .category(category)
                .build();

        expenseRepository.save(expense);
    }

    @Transactional
    public void updateExpense(ExpenseDto.UpdateRequest request, Long userId){

        validateDate.validateNotFutureAndServiceStartDate(request.getExpenseDate());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        ExpenseCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        Expense expense = expenseRepository.findByIdAndUser(request.getId(), user)
                .orElseThrow(()-> new BusinessException(ErrorCode.EXPENSE_NOT_FOUND));

        expense.updateExpense(request.getAmount()
                , request.getMemo()
                , request.getExpenseDate()
                , category);
    }

    @Transactional
    public void deleteExpense(Long id, Long userId){

        expenseRepository.deleteByIdAndUserId(id, userId);

    }

}

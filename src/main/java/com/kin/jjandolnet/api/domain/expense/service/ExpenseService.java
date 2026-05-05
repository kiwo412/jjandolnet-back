package com.kin.jjandolnet.api.domain.expense.service;

import com.kin.jjandolnet.api.domain.expense.dto.CategoryDto;
import com.kin.jjandolnet.api.domain.expense.dto.ExpenseDto;
import com.kin.jjandolnet.api.domain.expense.entity.Expense;
import com.kin.jjandolnet.api.domain.expense.entity.ExpenseCategory;
import com.kin.jjandolnet.api.domain.expense.entity.Income;
import com.kin.jjandolnet.api.domain.expense.repository.CategoryRepository;
import com.kin.jjandolnet.api.domain.expense.repository.ExpenseRepository;
import com.kin.jjandolnet.api.domain.expense.dto.IncomeDto;
import com.kin.jjandolnet.api.domain.expense.repository.IncomeRepository;
import com.kin.jjandolnet.api.domain.user.entity.User;
import com.kin.jjandolnet.api.domain.user.repository.UserRepository;
import com.kin.jjandolnet.global.error.exception.BusinessException;
import com.kin.jjandolnet.global.error.exception.ErrorCode;
import com.kin.jjandolnet.global.util.ValidateDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

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

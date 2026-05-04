package com.kin.jjandolnet.api.domain.expense.controller;

import com.kin.jjandolnet.api.domain.auth.UserPrincipal;
import com.kin.jjandolnet.api.domain.expense.dto.CategoryDto;
import com.kin.jjandolnet.api.domain.expense.dto.ExpenseDto;
import com.kin.jjandolnet.api.domain.expense.service.ExpenseService;
import com.kin.jjandolnet.api.domain.expense.dto.IncomeDto;
import com.kin.jjandolnet.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping("/expenseCategoryList")
    public ResponseEntity<ApiResponse<List<CategoryDto.Response>>> expenseCategoryList() {
        List<CategoryDto.Response> categoryList = expenseService.getCategoryList();
        return ResponseEntity.ok(ApiResponse.success("소비내역 카테고리 목록 조회가 완료되었습니다.", categoryList));
    }

    @GetMapping("/getExpenseList")
    public ResponseEntity<ApiResponse<List<ExpenseDto.Response>>> getExpenseList(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        List<ExpenseDto.Response> expenseList = expenseService.getExpenseList(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("소비내역 목록 조회가 완료되었습니다.", expenseList));
    }

    @GetMapping("/getIncome")
    public ResponseEntity<ApiResponse<IncomeDto.Response>> getIncome(
            @RequestParam String incomeDate,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        IncomeDto.Response incomeResponse = expenseService.getIncome(userPrincipal.getId(), incomeDate);
        return ResponseEntity.ok(ApiResponse.success(incomeDate+" 수입 조회가 완료되었습니다.", incomeResponse));
    }

    @PostMapping("/cuIncome")
    public ResponseEntity<ApiResponse<Void>> cuIncome(
            @Valid @RequestBody IncomeDto.CuRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        expenseService.cuIncome(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("수입이 저장되었습니다."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createExpense(
            @Valid @RequestBody ExpenseDto.CreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        expenseService.createExpense(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("소비내역이 저장되었습니다."));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateExpense(
            @Valid @RequestBody ExpenseDto.UpdateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        expenseService.updateExpense(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("소비내역이 수정되었습니다."));
    }



}

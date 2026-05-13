package com.kin.jjandolnet.api.domain.expense.controller;

import com.kin.jjandolnet.api.domain.auth.UserPrincipal;
import com.kin.jjandolnet.api.domain.expense.dto.*;
import com.kin.jjandolnet.api.domain.expense.service.ExpenseService;
import com.kin.jjandolnet.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "04. ExpenseController", description = " 소비 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(summary = "소비 카테고리 목록", security = @SecurityRequirement(name = "accessToken"))
    @GetMapping("/expenseCategoryList")
    public ResponseEntity<ApiResponse<List<CategoryDto.Response>>> expenseCategoryList() {
        List<CategoryDto.Response> categoryList = expenseService.getCategoryList();
        return ResponseEntity.ok(ApiResponse.success("소비내역 카테고리 목록 조회가 완료되었습니다.", categoryList));
    }

    @Operation(summary = "해당 월의 내 소비 내역 목록", security = @SecurityRequirement(name = "accessToken"))
    @GetMapping("/getExpenseList")
    public ResponseEntity<ApiResponse<List<ExpenseDto.Response>>> getExpenseList(
            @Parameter(example = "2026-05")
            @RequestParam String expenseDate,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        List<ExpenseDto.Response> expenseList = expenseService.getExpenseList(userPrincipal.getId(), expenseDate);
        return ResponseEntity.ok(ApiResponse.success("소비내역 목록 조회가 완료되었습니다.", expenseList));
    }

    @Operation(summary = "해당 월의 내 수입", security = @SecurityRequirement(name = "accessToken"))
    @GetMapping("/getIncome")
    public ResponseEntity<ApiResponse<IncomeDto.Response>> getIncome(
            @Parameter(example = "2026-05")
            @RequestParam String incomeDate,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        IncomeDto.Response incomeResponse = expenseService.getIncome(userPrincipal.getId(), incomeDate);
        return ResponseEntity.ok(ApiResponse.success(incomeDate+" 수입 조회가 완료되었습니다.", incomeResponse));
    }

    @Operation(summary = "해당 월의 내 소비 점수 그래프 데이터", security = @SecurityRequirement(name = "accessToken"))
    @GetMapping("/getMyScore")
    public ResponseEntity<ApiResponse<ScoreDto.Response>> getMyScore(
            @Parameter(example = "2026-05")
            @RequestParam String scoreDate,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        ScoreDto.Response scoreResponse = expenseService.getMyScore(userPrincipal.getId(), scoreDate);
        return ResponseEntity.ok(ApiResponse.success(scoreDate+" 짠돌력 조회가 완료되었습니다.", scoreResponse));
    }

    @Operation(summary = "해당 월의 내 소비 카테고리 비율 그래프 데이터", security = @SecurityRequirement(name = "accessToken"))
    @GetMapping("/getMyCategory")
    public ResponseEntity<ApiResponse<MyCategoryDto.Response>> getMyCategory(
            @Parameter(example = "2026-05")
            @RequestParam String categoryDate,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MyCategoryDto.Response categoryResponse = expenseService.getMyCategory(userPrincipal.getId(), categoryDate);
        return ResponseEntity.ok(ApiResponse.success(categoryDate+" 카테고리별 짠돌력 조회가 완료되었습니다.", categoryResponse));
    }

    @Operation(summary = "전체 회원 소비 그래프 데이터", security = @SecurityRequirement(name = "accessToken"))
    @GetMapping("/getMainChart")
    public ResponseEntity<ApiResponse<ChartDto.MainResponse>> getMainChart(
            @Valid @ModelAttribute ChartDto.searchCondition searchCondition
    ) {
        ChartDto.MainResponse response = expenseService.getMainChart(searchCondition);
        return ResponseEntity.ok(ApiResponse.success(" 짠한 차트(메인) 조회가 완료되었습니다.", response));
    }

    @Operation(summary = "내 소비와 특정 그룹 비교 그래프 데이터", security = @SecurityRequirement(name = "accessToken"))
    @GetMapping("/getSubChart1")
    public ResponseEntity<ApiResponse<ChartDto.SubResponse>> getSubChart1(
            @Valid @ModelAttribute ChartDto.searchCondition searchCondition,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ChartDto.SubResponse response = expenseService.getSubChart1(userPrincipal.getId(), searchCondition);
        return ResponseEntity.ok(ApiResponse.success(" 짠한 차트(sub) 조회가 완료되었습니다.", response));
    }

    @Operation(summary = "해당 월의 내 수입 저장 및 수정", security = @SecurityRequirement(name = "accessToken"))
    @PostMapping("/cuIncome")
    public ResponseEntity<ApiResponse<Void>> cuIncome(
            @Valid @RequestBody IncomeDto.CuRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        expenseService.cuIncome(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("수입이 저장되었습니다."));
    }

    @Operation(summary = "내 소비 내역 저장", security = @SecurityRequirement(name = "accessToken"))
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createExpense(
            @Valid @RequestBody ExpenseDto.CreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        expenseService.createExpense(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("소비내역이 저장되었습니다."));
    }

    @Operation(summary = "내 소비 내역 수정", security = @SecurityRequirement(name = "accessToken"))
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateExpense(
            @Valid @RequestBody ExpenseDto.UpdateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        expenseService.updateExpense(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("소비내역이 수정되었습니다."));
    }

    @Operation(summary = "내 소비 삭제", security = @SecurityRequirement(name = "accessToken"))
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        expenseService.deleteExpense(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("소비내역이 삭제되었습니다."));
    }



}

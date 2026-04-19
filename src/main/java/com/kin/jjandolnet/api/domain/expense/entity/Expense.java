package com.kin.jjandolnet.api.domain.expense.entity;

import com.kin.jjandolnet.api.domain.user.entity.User;
import com.kin.jjandolnet.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "expense")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long amount;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ExpenseCategory category;

    @Builder
    public Expense(Long amount, String memo, LocalDate expenseDate, User user, ExpenseCategory category) {
        this.amount = amount;
        this.memo = memo;
        this.expenseDate = expenseDate;
        this.user = user;
        this.category = category;
    }
}

package com.kin.jjandolnet.api.domain.expense.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "expense_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpenseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Builder
    public ExpenseCategory(String name) {
        this.name = name;
    }
}

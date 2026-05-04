package com.kin.jjandolnet.api.domain.expense.entity;

import com.kin.jjandolnet.api.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "income")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "income_date", nullable = false)
    private String incomeDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Income(Long amount, String incomeDate, User user) {
        this.amount = amount;
        this.incomeDate = incomeDate;
        this.user = user;
    }

    public void update(Long amount, String incomeDate) {
        this.amount = amount;
        this.incomeDate = incomeDate;
    }

}

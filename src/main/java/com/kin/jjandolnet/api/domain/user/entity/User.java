package com.kin.jjandolnet.api.domain.user.entity;

import com.kin.jjandolnet.api.domain.expense.entity.Expense;
import com.kin.jjandolnet.api.domain.post.entity.Comment;
import com.kin.jjandolnet.api.domain.rank.entity.RankHistory;
import com.kin.jjandolnet.api.domain.user.enums.Gender;
import com.kin.jjandolnet.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, unique = true)
    private String uuid;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(nullable = false, length = 50, unique = true)
    private String nickname;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "rank_score", nullable = false)
    private int rankScore = 0;

    @OneToMany(mappedBy = "user"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private List<RankHistory> rankHistories = new ArrayList<>();

    //syncRankScore() 계산을 위한 연관관계 편의 메서드
    public void addRankHistory(RankHistory history) {
        this.rankHistories.add(history);
        if (history.getUser() != this) {
            history.setUser(this);
        }
    }

    public void syncRankScore() {
        this.rankScore = this.rankHistories.stream()
                .mapToInt(RankHistory::getChangePoint)
                .sum();
    }

    // 빌더 패턴을 사용하여 객체 생성 (Setter 대신 사용)
    @Builder
    public User(String uuid, String password, String email, String nickname,
                LocalDate birthDate, Gender gender) {
        this.uuid = uuid;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
    }
}

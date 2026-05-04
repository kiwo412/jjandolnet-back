package com.kin.jjandolnet.api.domain.user.entity;

import com.kin.jjandolnet.api.domain.expense.entity.Income;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @OneToMany(mappedBy = "user"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private List<Income> incomes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRoles = new ArrayList<>();

    public void addRole(Role role) {
        UserRole userRole = UserRole.builder()
                .user(this)
                .role(role)
                .build();
        this.userRoles.add(userRole);
    }

    // 빌더 패턴을 사용하여 객체 생성 (Setter 대신 사용)
    @Builder
    public User(String uuid, String password, String email, String nickname,
                LocalDate birthDate, Gender gender, Address address, Job job, List<UserRole> userRoles) {
        this.uuid = uuid;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.job = job;
        this.userRoles = (userRoles != null) ? userRoles : new ArrayList<>();
    }
}

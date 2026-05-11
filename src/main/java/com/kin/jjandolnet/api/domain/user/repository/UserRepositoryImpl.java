package com.kin.jjandolnet.api.domain.user.repository;

import com.kin.jjandolnet.api.domain.user.dto.UserDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.kin.jjandolnet.api.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<String> findEmailByCondition(UserDto.FindIdRequest request) {
        String result = queryFactory
                .select(user.email)
                .from(user)
                .where(
                        user.birthDate.eq(request.getBirthDate()),
                        user.gender.eq(request.getGender()),
                        user.address.id.eq(request.getAddressId()),
                        user.job.id.eq(request.getJobId())
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }
}

package com.kin.jjandolnet.api.domain.user.repository;

import com.kin.jjandolnet.api.domain.user.dto.UserDto;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<String> findEmailByCondition(UserDto.FindIdRequest request);
}

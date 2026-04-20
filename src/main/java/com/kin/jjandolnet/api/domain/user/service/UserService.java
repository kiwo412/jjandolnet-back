package com.kin.jjandolnet.api.domain.user.service;

import com.kin.jjandolnet.api.domain.user.dto.UserDto;
import com.kin.jjandolnet.api.domain.user.entity.Role;
import com.kin.jjandolnet.api.domain.user.entity.User;
import com.kin.jjandolnet.api.domain.user.exception.UserException;
import com.kin.jjandolnet.api.domain.user.repository.RoleRepository;
import com.kin.jjandolnet.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserDto.CreateRequest request) {

        //중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException.DuplicateEmailException();
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new UserException.DuplicateNicknameException();
        }

        //회원가입 시 role_user 권한 부여
        //admin 권한은 차후에 기존 admin이 승격 시키는 걸로 하자는 게 내 신조입니다.
        Role defaultRole = roleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("기본 권한을 찾을 수 없습니다."));

        String uuid = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = request.toEntity(uuid, encodedPassword);
        user.addRole(defaultRole);

        userRepository.save(user);
    }

}

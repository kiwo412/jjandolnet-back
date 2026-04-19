package com.kin.jjandolnet.api.domain.user.service;

import com.kin.jjandolnet.api.domain.user.dto.UserDto;
import com.kin.jjandolnet.api.domain.user.entity.User;
import com.kin.jjandolnet.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserDto.CreateRequest request) {

        String uuid = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = request.toEntity(uuid, encodedPassword);

        userRepository.save(user);
    }

}

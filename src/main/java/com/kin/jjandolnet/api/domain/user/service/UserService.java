package com.kin.jjandolnet.api.domain.user.service;

import com.kin.jjandolnet.api.domain.user.dto.AddressDto;
import com.kin.jjandolnet.api.domain.user.dto.JobDto;
import com.kin.jjandolnet.api.domain.user.dto.UserDto;
import com.kin.jjandolnet.api.domain.user.entity.Address;
import com.kin.jjandolnet.api.domain.user.entity.Job;
import com.kin.jjandolnet.api.domain.user.entity.Role;
import com.kin.jjandolnet.api.domain.user.entity.User;
import com.kin.jjandolnet.api.domain.user.exception.UserException;
import com.kin.jjandolnet.api.domain.user.repository.AddressRepository;
import com.kin.jjandolnet.api.domain.user.repository.JobRepository;
import com.kin.jjandolnet.api.domain.user.repository.RoleRepository;
import com.kin.jjandolnet.api.domain.user.repository.UserRepository;
import com.kin.jjandolnet.global.error.exception.BusinessException;
import com.kin.jjandolnet.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    private final JobRepository jobRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserDto.CreateRequest request) {

        //중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException.DuplicateEmailException();
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new UserException.DuplicateNicknameException();
        }

        //회원가입 시 role_user 권한 부여
        Role defaultRole = roleRepository.findById(1L)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_FOUND));

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_NOT_FOUND));

        String uuid = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = request.toEntity(uuid, encodedPassword, address, job);
        user.addRole(defaultRole);

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<AddressDto.Response> getAddressList() {
       return addressRepository.findAll()
               .stream()
               .map(AddressDto.Response::from)
               .toList();
    }

    @Transactional(readOnly = true)
    public List<JobDto.Response> getJobList() {
        return jobRepository.findAll()
                .stream()
                .map(JobDto.Response::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDto.Response getUserDetail(Long userId) {
        return userRepository.findById(userId).map(UserDto.Response::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateUserDetail(UserDto.UpdateRequest request, Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!user.getNickname().equals(request.getNickname())
                && userRepository.existsByNickname(request.getNickname())) {
            throw new UserException.DuplicateNicknameException();
        }

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        user.updateUser(request.getNickname(), encodedPassword, address, job);

    }

}

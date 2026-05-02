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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
        //admin 권한은 차후에 기존 admin이 승격 시키는 걸로 하자는 게 내 신조입니다.
        Role defaultRole = roleRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("기본 권한을 찾을 수 없습니다."));

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new RuntimeException("거주 지역 정보를 찾을 수 없습니다."));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException("직업 정보를 찾을 수 없습니다."));

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

}

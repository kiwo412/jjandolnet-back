package com.kin.jjandolnet.api.domain.user.repository;

import com.kin.jjandolnet.api.domain.user.entity.Role;
import com.kin.jjandolnet.api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

}

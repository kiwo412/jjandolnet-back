package com.kin.jjandolnet.api.domain.user.repository;

import com.kin.jjandolnet.api.domain.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}

package com.kin.jjandolnet.api.domain.user.repository;

import com.kin.jjandolnet.api.domain.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

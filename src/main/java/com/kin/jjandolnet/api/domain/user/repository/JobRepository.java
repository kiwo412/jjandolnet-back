package com.kin.jjandolnet.api.domain.user.repository;

import com.kin.jjandolnet.api.domain.user.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}

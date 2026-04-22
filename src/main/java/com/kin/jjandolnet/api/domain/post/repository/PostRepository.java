package com.kin.jjandolnet.api.domain.post.repository;

import com.kin.jjandolnet.api.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>{

}

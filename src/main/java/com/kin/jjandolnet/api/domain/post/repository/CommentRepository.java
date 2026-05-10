package com.kin.jjandolnet.api.domain.post.repository;

import com.kin.jjandolnet.api.domain.post.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>{

    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.post.id = :postId")
    Page<Comment> findByPostId(Long postId, Pageable pageable);

    Optional<Comment> findByIdAndUser_Id(Long commentId, Long userId);
    void deleteByIdAndUser_Id(Long commentId, Long userId);
}

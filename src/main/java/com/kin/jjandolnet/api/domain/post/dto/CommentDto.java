package com.kin.jjandolnet.api.domain.post.dto;

import com.kin.jjandolnet.api.domain.post.entity.Comment;
import com.kin.jjandolnet.api.domain.post.entity.Post;
import com.kin.jjandolnet.api.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class CommentDto {

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private String content;
        private String userNickname;
        private LocalDateTime createdAt;

        public static Response from(Comment comment) {
            return Response.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .userNickname(comment.getUser().getNickname())
                    .createdAt(comment.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CreateRequest {
        private String content;
        private Long postId;

        public Comment toEntity(Post post, User user) {
            return Comment.builder()
                    .content(content)
                    .post(post)
                    .user(user)
                    .build();
        }
    }
}

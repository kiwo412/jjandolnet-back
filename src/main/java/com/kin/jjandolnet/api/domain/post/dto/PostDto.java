package com.kin.jjandolnet.api.domain.post.dto;

import com.kin.jjandolnet.api.domain.post.entity.Post;
import com.kin.jjandolnet.api.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class PostDto {

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private String category;
        private String title;
        private String content;
        private int viewCount;
        private String status;
        private String userNickname;
        private LocalDateTime createdAt;

        public static Response from(Post post) {
            return Response.builder()
                    .id(post.getId())
                    .category(post.getCategory())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .viewCount(post.getViewCount())
                    .status(post.getStatus())
                    .userNickname(post.getUser().getNickname())
                    .createdAt(post.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CreateRequest {
        private String category;
        private String title;
        private String content;

        public Post toEntity(User user) {
            return Post.builder()
                    .category(category)
                    .title(title)
                    .content(content)
                    .user(user)
                    .viewCount(0)
                    .status("ACTIVE") // 기본 상태값 설정 가능
                    .build();
        }
    }
}

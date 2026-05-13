package com.kin.jjandolnet.api.domain.post.dto;

import com.kin.jjandolnet.api.domain.post.entity.Post;
import com.kin.jjandolnet.api.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

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
        private String uuid;
        private String nickname;
        private LocalDate createdAt;

        public static Response from(Post post) {
            return Response.builder()
                    .id(post.getId())
                    .category(post.getCategory())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .viewCount(post.getViewCount())
                    .status(post.getStatus())
                    .uuid(post.getUser().getUuid())
                    .nickname(post.getUser().getNickname())
                    .createdAt(post.getCreatedAt().toLocalDate())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class SearchRequest {
        @Schema(example = "title")
        private String filter;
        @Schema(example = "nullable")
        private String keyword;
    }

    @Getter
    @Builder
    public static class CreateRequest {
        @Schema(example = "FREE")
        @NotBlank(message = "카테고리는 필수입니다.")
        private String category;
        @Schema(example = "test title")
        @NotBlank(message = "제목을 입력해주세요.")
        private String title;
        @Schema(example = "test content")
        @NotBlank(message = "내용을 입력해주세요.")
        private String content;

        public Post toEntity(User user) {
            return Post.builder()
                    .category(category)
                    .title(title)
                    .content(content)
                    .user(user)
                    .viewCount(0)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class updateRequest {
        @Schema(example = "1")
        @NotNull(message = "ID는 필수입니다.")
        private Long id;
        @Schema(example = "update title")
        @NotBlank(message = "제목을 입력해주세요.")
        private String title;
        @Schema(example = "update content")
        @NotBlank(message = "내용을 입력해주세요.")
        private String content;
    }
}

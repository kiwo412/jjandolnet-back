package com.kin.jjandolnet.api.domain.post.dto;

import com.kin.jjandolnet.api.domain.post.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class CommentDto {

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private String uuid;
        private String content;
        private String nickname;
        private LocalDateTime createdAt;

        public static Response from(Comment comment) {
            return Response.builder()
                    .id(comment.getId())
                    .uuid(comment.getUser().getUuid())
                    .content(comment.getContent())
                    .nickname(comment.getUser().getNickname())
                    .createdAt(comment.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CreateRequest {
        @NotBlank(message = "댓글을 입력해주세요.")
        private String content;
        @NotNull(message = "글 정보가 없습니다.")
        private Long postId;
    }

    @Getter
    @Builder
    public static class UpdateRequest {
        @NotBlank(message = "댓글을 입력해주세요.")
        private String content;
        @NotNull(message = "글 정보가 없습니다.")
        private Long postId;
        @NotNull(message = "댓글 정보가 없습니다.")
        private Long id;
    }
}

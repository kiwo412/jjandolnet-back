package com.kin.jjandolnet.api.domain.user.dto;

import com.kin.jjandolnet.api.domain.user.entity.User;
import com.kin.jjandolnet.api.domain.user.enums.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserDto {

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private String uuid;
        private String email;
        private String nickname;
        private LocalDate birthDate;
        private Gender gender;
        private int rankScore;
        private LocalDateTime createdAt;

        public static Response from(User user) {
            return Response.builder()
                    .id(user.getId())
                    .uuid(user.getUuid())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .birthDate(user.getBirthDate())
                    .gender(user.getGender())
                    .rankScore(user.getRankScore())
                    .createdAt(user.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CreateRequest {
        private String email;
        private String password;
        private String nickname;
        private LocalDate birthDate;
        private Gender gender;

        public User toEntity(String uuid, String encodedPassword) {
            return User.builder()
                    .uuid(uuid)
                    .email(email)
                    .password(encodedPassword)
                    .nickname(nickname)
                    .birthDate(birthDate)
                    .gender(gender)
                    .build();
        }
    }
}

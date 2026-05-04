package com.kin.jjandolnet.api.domain.user.dto;

import com.kin.jjandolnet.api.domain.user.entity.Address;
import com.kin.jjandolnet.api.domain.user.entity.Job;
import com.kin.jjandolnet.api.domain.user.entity.User;
import com.kin.jjandolnet.api.domain.user.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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
        private LocalDateTime createdAt;

        public static Response from(User user) {
            return Response.builder()
                    .id(user.getId())
                    .uuid(user.getUuid())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .birthDate(user.getBirthDate())
                    .gender(user.getGender())
                    .createdAt(user.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CreateRequest {

        @Schema(description = "사용자 이메일", example = "test1234@naver.com")
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
                message = "이메일 형식이 올바르지 않습니다. (예: user@example.com)")
        private String email;

        @Schema(example = "password1234!")
        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        private String password;

        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        @Size(min = 2, max = 10, message = "닉네임은 2~10자 사이여야 합니다.")
        private String nickname;

        @NotNull(message = "생년월일은 필수 입력값입니다.")
        @Past(message = "생년월일을 제대로 입력해 주세요.")
        private LocalDate birthDate;

        @NotNull(message = "성별을 선택해주세요.")
        private Gender gender;

        @NotNull(message = "거주 지역을 선택해주세요.")
        private Long addressId;

        @NotNull(message = "직업을 선택해주세요.")
        private Long jobId;

        public User toEntity(String uuid, String encodedPassword, Address address, Job job) {
            return User.builder()
                    .uuid(uuid)
                    .email(email)
                    .password(encodedPassword)
                    .nickname(nickname)
                    .birthDate(birthDate)
                    .gender(gender)
                    .address(address)
                    .job(job)
                    .build();
        }
    }
}

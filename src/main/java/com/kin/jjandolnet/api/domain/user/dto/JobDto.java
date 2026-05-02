package com.kin.jjandolnet.api.domain.user.dto;

import com.kin.jjandolnet.api.domain.user.entity.Job;
import lombok.Builder;
import lombok.Getter;

public class JobDto {

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private String name;

        public static Response from(Job job) {
            return Response.builder()
                    .id(job.getId())
                    .name(job.getName())
                    .build();
        }
    }
}

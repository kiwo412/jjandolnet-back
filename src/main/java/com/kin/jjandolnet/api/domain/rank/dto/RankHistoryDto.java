package com.kin.jjandolnet.api.domain.rank.dto;

import com.kin.jjandolnet.api.domain.rank.entity.RankHistory;
import lombok.Builder;
import lombok.Getter;

public class RankHistoryDto {

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private int changePoint;
        private String reason;
        private Long userId;
        private Long postId;

        public static Response from(RankHistory rankHistory) {
            return Response.builder()
                    .id(rankHistory.getId())
                    .changePoint(rankHistory.getChangePoint())
                    .reason(rankHistory.getReason())
                    .userId(rankHistory.getUser().getId())
                    .postId(rankHistory.getPost().getId())
                    .build();
        }
    }
}

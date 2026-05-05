package com.kin.jjandolnet.api.domain.expense.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ExpenseScoreLevel {

    OVER(Integer.MIN_VALUE, -1, "수입보다 소비가 많습니다. 정말 심각한 상태입니다. 특단의 조치가 필요합니다."),
    BAD(0, 20, "위험 단계입니다! 지갑을 잠시 닫아두는 건 어떨까요?"),
    AVERAGE(21, 50, "조금씩 소비가 늘고 있어요. 주의가 필요해요."),
    GOOD(51, 80, "현명한 소비 중이에요. 당신이 진정한 짠돌이! 이대로만 가시죠!!"),
    EXCELLENT(81, 100, "밥은 먹고 다니시는 거죠...? 서울 아파트가 멀지 않았어요!!  ");

    private final int minScore;
    private final int maxScore;
    private final String message;

    public static ExpenseScoreLevel findByScore(long score) {
        return Arrays.stream(values())
                .filter(level -> score >= level.minScore && score <= level.maxScore)
                .findFirst()
                .orElse(OVER);
    }
}

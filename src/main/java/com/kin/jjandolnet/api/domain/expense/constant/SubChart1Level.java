package com.kin.jjandolnet.api.domain.expense.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.DoublePredicate;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum SubChart1Level {
    OVER_CONSUMPTION(
            p -> p > 100,
            "%s 평균보다 %.1f%% 더 많이 쓰셨네요! 조금 아껴보는건 어떨까요? 😅"
    ),
    UNDER_CONSUMPTION(
            p -> p < 100 && p > 10,
            "%s 평균보다 %.1f%% 를 아꼈어요. 아주 잘하고 계세요 👍"
    ),
    SUPER_SAVING(
            p -> p <= 10 && p > 0,
            "%s 평균보다 %.1f%% 나 절약이라니.... 당신은 진정한 짠돌이 마스터! 🏆"
    ),
    AVERAGE(
            p -> true,
            "%s 평균과 비슷한 소비패턴이에요. 훌륭해요! ✨"
    );

    private final DoublePredicate predicate;
    private final String messageTemplate;

    // 퍼센트에 맞는 상태를 찾아주는 정적 메서드
    public static SubChart1Level of(double percent) {
        return Stream.of(values())
                .filter(status -> status.predicate.test(percent))
                .findFirst()
                .orElse(AVERAGE);
    }

    // 메시지를 포맷팅해서 반환하는 메서드
    public String formatMessage(String groupValue, double percent) {
        if (this == OVER_CONSUMPTION) {
            return String.format(messageTemplate, groupValue, percent - 100);
        } else if (this == UNDER_CONSUMPTION|| this == SUPER_SAVING) {
            return String.format(messageTemplate, groupValue, 100 - percent);
        }
        return String.format(messageTemplate, groupValue);
    }
}

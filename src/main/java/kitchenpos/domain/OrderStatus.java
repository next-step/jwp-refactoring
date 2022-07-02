package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isCompletion() {
        return this == COMPLETION;
    }

    public static List<OrderStatus> findNotCompletionStatus() {
        return Arrays.stream(values())
                .filter(status -> !status.isCompletion())
                .collect(Collectors.toList());
    }
}

package kichenpos.order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    private static final List<OrderStatus> STATUS_BEFORE_COMPLETION = Arrays.asList(COOKING, MEAL);

    public boolean isCompletion() {
        return this == COMPLETION;
    }

    public static List<OrderStatus> findNotCompletionStatus() {
        return STATUS_BEFORE_COMPLETION;
    }
}

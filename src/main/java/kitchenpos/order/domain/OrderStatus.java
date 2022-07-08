package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static List<OrderStatus> canNotChangeOrderTableStatuses() {
        return Arrays.asList(COOKING, MEAL);
    }

    public boolean isCompletion() {
        return this == COMPLETION;
    }
}

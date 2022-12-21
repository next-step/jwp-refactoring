package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING,
    MEAL,
    COMPLETION;

    public boolean isCompleted() {
        return this == COMPLETION;
    }

    public static List<OrderStatus> getNotCompletedStatuses() {
        return Arrays.asList(COOKING, MEAL);
    }
}

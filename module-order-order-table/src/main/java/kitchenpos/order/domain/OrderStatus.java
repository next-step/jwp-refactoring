package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;
    private static final List<OrderStatus> UNCOMPLETED_STATUSES = Arrays.asList(COOKING, MEAL);

    public static List<OrderStatus> getCannotUngroupTableGroupStatus() {
        return UNCOMPLETED_STATUSES;
    }
}

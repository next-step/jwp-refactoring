package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;
    private static final List<OrderStatus> UNCHANGEABLE_STATUS = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

    public static boolean isUnchangeableStatus(OrderStatus status) {
        return UNCHANGEABLE_STATUS.contains(status);
    }
}

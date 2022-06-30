package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static final OrderStatus DEFAULT = OrderStatus.COOKING;
    public static final List<OrderStatus> NOT_COMPLETED = Arrays.asList(
            OrderStatus.COOKING,
            OrderStatus.MEAL);
}

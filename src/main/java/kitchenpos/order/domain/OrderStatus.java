package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING,
    MEAL,
    COMPLETION;

    public static final List<OrderStatus> INCOMPLETE_STATUSES = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

}

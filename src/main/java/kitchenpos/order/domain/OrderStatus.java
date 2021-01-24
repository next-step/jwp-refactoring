package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static final List<OrderStatus> NOT_CHANGE_ORDER_STATUS = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

}

package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static List<OrderStatus> onGoingOrderStatus() {
        return Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
    }
}

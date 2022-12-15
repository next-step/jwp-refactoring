package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static List<OrderStatus> cantChangeEmpty() {
        return Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
    }

    public static List<OrderStatus> cantUngroup() {
        return Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
    }
}

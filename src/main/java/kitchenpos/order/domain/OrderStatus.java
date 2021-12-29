package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.Objects;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus getOrderStatus(String orderStatusName) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> Objects.equals(orderStatus.name(), orderStatusName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("주문 상태가 올바르지 않습니다. orderStatus : " + orderStatusName));
    }
}

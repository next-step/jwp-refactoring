package kitchenpos.domain.order;

import kitchenpos.domain.order.exceptions.OrderStatusNotFoundException;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus find(String name) {
        return Arrays.stream(OrderStatus.values())
                .filter(it -> it.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new OrderStatusNotFoundException("존재하지 않는 주문상태입니다."));
    }

    public boolean canChange() {
        return !this.equals(OrderStatus.COMPLETION);
    }
}

package kitchenpos.domain;

import java.util.List;
import java.util.Objects;

public class Orders {
    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean isNotCompleted(Long orderTableId) {
        return orders.stream()
            .anyMatch(order -> Objects.equals(order.getOrderTableId(), orderTableId) &&
                order.isNotCompleted());
    }

    public boolean isNotCompleted() {
        return orders.stream()
            .anyMatch(Order::isNotCompleted);
    }
}

package kitchenpos.order.domain;

import java.util.List;
import java.util.Objects;

public class Orders {
    private final List<Order> orders;

    public Orders(final List<Order> orders) {
        this.orders = orders;
    }

    public boolean isNotCompleted(final Long orderTableId) {
        return orders.stream()
            .anyMatch(order -> Objects.equals(order.getOrderTableId(), orderTableId) &&
                order.isNotCompleted());
    }

    public boolean isNotCompleted() {
        return orders.stream()
            .anyMatch(Order::isNotCompleted);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Orders orders1 = (Orders)o;
        return Objects.equals(orders, orders1.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders);
    }
}

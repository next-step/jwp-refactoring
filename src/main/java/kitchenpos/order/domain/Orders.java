package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Orders {

    public static final String MESSAGE_VALIDATE_EMPTY_CHANGABLE = "주문이 변경 가능한 상태여야 합니다.";

    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void validateEmptyChangable() {
        orders.forEach(this::validateOrder);
    }

    public boolean isChangable() {
        return orders.stream()
                .allMatch(Order::isChangable);
    }

    private void validateOrder(Order order) {
        if (!order.isChangable()) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_EMPTY_CHANGABLE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orders orders1 = (Orders) o;
        return Objects.equals(orders, orders1.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders);
    }
}

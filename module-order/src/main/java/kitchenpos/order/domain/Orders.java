package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.order.consts.OrderStatus;

public class Orders {

    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean containOrderStatus(OrderStatus orderStatus) {
        return orders.stream()
                .map(Order::getOrderStatus)
                .anyMatch(orderStatus::equals);
    }

    public List<Order> getOrders() {
        return orders;
    }
}

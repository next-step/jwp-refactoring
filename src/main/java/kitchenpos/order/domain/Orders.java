package kitchenpos.order.domain;

import java.util.List;

public class Orders {

    private List<Order> orders;

    private Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders from(List<Order> orders) {
        return new Orders(orders);
    }

    public boolean anyMatchedIn(List<OrderStatus> orderStatuses) {
        return orders.stream().anyMatch(
                it -> orderStatuses.contains(it.getOrderStatus())
        );
    }

    public void add(Order order) {
        if (this.orders.contains(order)) {
            return;
        }

        this.orders.add(order);
    }
}

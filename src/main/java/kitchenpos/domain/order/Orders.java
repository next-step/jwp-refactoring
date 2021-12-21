package kitchenpos.domain.order;

import java.util.List;

public class Orders {

    List<Order> orders;

    private Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders of(List<Order> orders) {
        return new Orders(orders);
    }

    public boolean isOrdersAllCompleted() {
        return orders.stream()
            .allMatch(Order::isComplete);
    }
}

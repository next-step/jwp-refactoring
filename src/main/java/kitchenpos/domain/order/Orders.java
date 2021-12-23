package kitchenpos.domain.order;

import java.util.List;
import java.util.stream.Collectors;

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

    public Orders matchedBy(OrderTable orderTable) {
        return orders.stream()
            .filter(order -> order.isMatchOrderTable(orderTable))
            .collect(Collectors.collectingAndThen(Collectors.toList(), Orders::of));
    }

    public List<OrderTable> getOrderTables() {
        return orders.stream()
            .map(Order::getOrderTable)
            .collect(Collectors.toList());
    }
}

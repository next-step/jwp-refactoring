package kitchenpos.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderTable;

import java.util.ArrayList;
import java.util.List;

public class TestOrderFactory {
    public static Order create(Long orderTableId, OrderStatus status, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, status, new OrderLineItems(orderLineItems));
    }

    public static Order createCompleteOrder() {
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = new Order(
                orderTable.getId(),
                OrderStatus.COMPLETION,
                new OrderLineItems(new ArrayList<>())
        );
        orderTable.setEmpty(true);
        return order;
    }

    public static Order createCompleteOrderWith(OrderTable orderTable) {
        Order order = new Order(
                orderTable.getId(),
                OrderStatus.COMPLETION,
                new OrderLineItems(new ArrayList<>())
        );
        orderTable.setEmpty(true);
        return order;
    }

    public static Order createMealOrder() {
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        return new Order(
                orderTable.getId(),
                OrderStatus.MEAL,
                new OrderLineItems(new ArrayList<>())
        );
    }
}

package kitchenpos;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderFixture {
    private OrderFixture() {
    }

    public static Order getOrder(long id, long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return Order.generate(id, orderTableId, orderStatus, orderLineItems);
    }

    public static Order getOrder(long id, long orderTableId, List<OrderLineItem> orderLineItems) {
        return Order.generate(id, orderTableId, orderLineItems);
    }

    public static List<OrderLineItem> getOrderLineItems(OrderLineItem... orderLineItems) {
        return Arrays.stream(orderLineItems)
                .collect(Collectors.toList());
    }

    public static OrderLineItem getOrderLineItem(long menuId, int quantity) {
        return OrderLineItem.of(menuId, quantity);
    }
}

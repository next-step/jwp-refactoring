package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import java.util.Arrays;
import java.util.List;

public class OrderFixture {

    public static Order 주문(Long orderTableId, OrderLineItem orderLineItem) {
        return 주문(orderTableId, Arrays.asList(orderLineItem), OrderStatus.COOKING);
    }

    public static Order 주문(Long orderTableId, List<OrderLineItem> orderLineItems, OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order 주문(OrderTable orderTable, OrderLineItem orderLineItem, OrderStatus orderStatus) {
        return 주문(orderTable.getId(), Arrays.asList(orderLineItem), orderStatus);
    }

    public static Order 주문(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return 주문(orderTable.getId(), orderLineItems, OrderStatus.COOKING);
    }

    public static Order 주문(OrderTable orderTable, OrderLineItem orderLineItem) {
        return 주문(orderTable.getId(), Arrays.asList(orderLineItem), OrderStatus.COOKING);
    }
}

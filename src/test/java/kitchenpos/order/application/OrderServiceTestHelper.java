package kitchenpos.order.application;

import java.util.Arrays;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderServiceTestHelper {
    public static OrderLineItem 주문_상품_생성(Long id, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(id);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static Order 주문_생성(Long id, Long orderTableId, OrderStatus orderStatus, OrderLineItem... orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus.name());
        order.setOrderLineItems(Arrays.asList(orderLineItems));
        return order;
    }
}

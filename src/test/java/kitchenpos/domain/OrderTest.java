package kitchenpos.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderTest {

    public static Order 주문_생성(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        Order result = new Order();

        result.setOrderTableId(orderTableId);
        result.setOrderStatus(orderStatus.name());
        result.setOrderLineItems(orderLineItems);
        result.setOrderedTime(LocalDateTime.now());

        return result;
    }
}

package kitchenpos.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderTest {

    public static Order 주문_생성(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }
}

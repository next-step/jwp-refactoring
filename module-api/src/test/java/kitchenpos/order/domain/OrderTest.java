package kitchenpos.order.domain;

import java.util.List;

public class OrderTest {

    public static Order 주문_생성(
            Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, orderLineItems);
    }

    public static Order 주문_생성(
            Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderLineItems);
    }
}

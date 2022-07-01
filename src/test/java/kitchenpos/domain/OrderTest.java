package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class OrderTest {

    public static Order 주문_생성(
            Long orderTableId, OrderStatus orderStatus,
            LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order 주문_생성(
            Long id, Long orderTableId, OrderStatus orderStatus,
            LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}

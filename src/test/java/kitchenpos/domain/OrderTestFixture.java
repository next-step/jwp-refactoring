package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class OrderTestFixture {
    public static Order createOrder(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order createOrder(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return Order.of(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static OrderLineItem createOrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        return OrderLineItem.of(seq, orderId, menuId, quantity);
    }
}

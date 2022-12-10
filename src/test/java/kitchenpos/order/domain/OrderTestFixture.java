package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderTestFixture {

    public static Order generateOrder(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order generateOrder(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return Order.of(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}

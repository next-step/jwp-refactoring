package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class OrderTestFixture {

    public static Order generateOrder(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order generateOrder(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return Order.of(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}

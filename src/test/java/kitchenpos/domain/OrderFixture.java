package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {
    public static Order createOrder(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order createOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, null, null, orderLineItems);
    }
}

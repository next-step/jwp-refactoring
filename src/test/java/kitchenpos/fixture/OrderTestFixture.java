package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderTestFixture {

    public static Order createOrder(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order createOrder(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return Order.of(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}

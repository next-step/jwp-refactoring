package kitchenpos.__fixture__;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderTestFixture {
    public static Order 주문_생성(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                              final OrderLineItem... orderLineItems) {
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order 빈_주문_생성(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime) {
        return new Order(orderTableId, orderStatus, orderedTime);
    }
}

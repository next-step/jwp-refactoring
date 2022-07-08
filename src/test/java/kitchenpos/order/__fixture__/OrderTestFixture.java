package kitchenpos.order.__fixture__;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.request.OrderLineItemRequest;
import kitchenpos.order.request.OrderRequest;

public class OrderTestFixture {
    public static Order 주문_생성(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                              final List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order 빈_주문_생성(final Long orderTableId, final OrderStatus orderStatus,
                                final LocalDateTime orderedTime) {
        return new Order(orderTableId, orderStatus, orderedTime);
    }

    public static OrderRequest 주문_요청_생성(final Long orderTableId,
                                        final List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }

    public static OrderRequest 빈_주문_요청_생성(final Long orderTableId) {
        return new OrderRequest(orderTableId);
    }
}

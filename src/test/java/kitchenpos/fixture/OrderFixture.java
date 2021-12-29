package kitchenpos.fixture;

import java.util.List;
import kitchenpos.common.domain.Validator;
import kitchenpos.orders.order.domain.Order;
import kitchenpos.orders.order.domain.OrderLineItems;
import kitchenpos.orders.order.domain.OrderStatus;
import kitchenpos.orders.order.dto.OrderChangeStatusRequest;
import kitchenpos.orders.order.dto.OrderLineItemRequest;
import kitchenpos.orders.order.dto.OrderRequest;

public class OrderFixture {

    private OrderFixture() {
    }

    public static Order of(
        final Long id,
        final Long orderTableId,
        final OrderLineItems orderLineItems,
        final Validator<Order> validator
    ) {
        return new Order(id, orderTableId, orderLineItems, validator);
    }

    public static Order of(
        final Long orderTableId,
        final OrderLineItems orderLineItems,
        final Validator<Order> validator
    ) {
        return of(null, orderTableId, orderLineItems, validator);
    }

    public static OrderRequest ofRequest(
        final Long orderTableId,
        final List<OrderLineItemRequest> orderLineItems
    ) {
        return new OrderRequest(orderTableId, orderLineItems);
    }

    public static OrderChangeStatusRequest ofChangeStatusRequest(final OrderStatus status) {
        return new OrderChangeStatusRequest(status);
    }
}

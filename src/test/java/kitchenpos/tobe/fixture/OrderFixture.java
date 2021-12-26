package kitchenpos.tobe.fixture;

import java.util.List;
import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.orders.domain.order.Order;
import kitchenpos.tobe.orders.domain.order.OrderLineItems;
import kitchenpos.tobe.orders.domain.order.OrderStatus;
import kitchenpos.tobe.orders.dto.order.OrderChangeStatusRequest;
import kitchenpos.tobe.orders.dto.order.OrderLineItemRequest;
import kitchenpos.tobe.orders.dto.order.OrderRequest;

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

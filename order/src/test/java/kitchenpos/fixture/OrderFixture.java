package kitchenpos.fixture;

import java.util.List;
import kitchenpos.domain.Validator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderChangeStatusRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

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

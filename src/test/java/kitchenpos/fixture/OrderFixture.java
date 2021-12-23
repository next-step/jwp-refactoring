package kitchenpos.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {

    private OrderFixture() {
        throw new UnsupportedOperationException();
    }

    public static Order create(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTable, orderStatus, orderedTime, OrderLineItems.of(orderLineItems));
    }
}

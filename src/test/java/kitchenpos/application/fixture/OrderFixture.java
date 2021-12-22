package kitchenpos.application.fixture;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

public class OrderFixture {

    private OrderFixture() {
    }

    public static Order create(OrderStatus orderStatus) {
        return new Order(orderStatus);
    }

    public static Order create(final Long id, final OrderTable orderTable,
        final List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, OrderStatus.COOKING, orderLineItems);
    }

    public static Order create(final Long id, final OrderTable orderTable,
        final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderStatus, orderLineItems);
    }
}

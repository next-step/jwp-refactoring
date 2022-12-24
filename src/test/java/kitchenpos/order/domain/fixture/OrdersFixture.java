package kitchenpos.order.domain.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderMenu;

import static kitchenpos.order.domain.fixture.OrderLineItemsFixture.orderLineItemsA;

public class OrdersFixture {

    public static Order orderA(Long orderTableId, OrderMenu orderMenu) {
        return new Order(orderTableId, orderLineItemsA(orderMenu));
    }
}

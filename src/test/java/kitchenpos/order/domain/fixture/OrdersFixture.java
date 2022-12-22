package kitchenpos.order.domain.fixture;

import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.Orders;

import static kitchenpos.order.domain.fixture.OrderLineItemsFixture.orderLineItemsA;

public class OrdersFixture {

    public static Orders orderA(Long orderTableId, OrderMenu orderMenu) {
        return new Orders(orderTableId, orderLineItemsA(orderMenu));
    }
}

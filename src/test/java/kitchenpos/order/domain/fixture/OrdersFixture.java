package kitchenpos.order.domain.fixture;

import kitchenpos.order.domain.Orders;

import static kitchenpos.order.domain.fixture.OrderLineItemsFixture.orderLineItemsA;

public class OrdersFixture {

    public static Orders orderA(Long orderTableId) {
        return new Orders(orderTableId, orderLineItemsA());
    }
}

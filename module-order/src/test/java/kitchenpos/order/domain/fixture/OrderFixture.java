package kitchenpos.order.domain.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;

import static kitchenpos.order.domain.fixture.OrderLineItemsFixture.orderLineItemsA;

public class OrderFixture {

    public static Order orderA(Long orderTableId) {
        return new Order(1L, orderTableId, new OrderLineItems(orderLineItemsA()));
    }
}

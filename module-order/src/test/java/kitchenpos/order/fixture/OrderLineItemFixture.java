package kitchenpos.order.fixture;

import kitchenpos.common.domain.Quantity;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {

    private OrderLineItemFixture() {
        throw new UnsupportedOperationException();
    }

    public static OrderLineItem create(Long seq, Order order, Long menuId, long quantity) {
        return OrderLineItem.of(seq, order, menuId, Quantity.of(quantity));
    }
}

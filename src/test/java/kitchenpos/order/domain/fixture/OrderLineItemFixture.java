package kitchenpos.order.domain.fixture;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem OrderLineItem() {
        return new OrderLineItem(null, 1L, 3);
    }
}

package kitchenpos.order.domain.fixture;

import kitchenpos.common.Quantity;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem OrderLineItem() {
        return new OrderLineItem(null, 1L, new Quantity(1));
    }
}

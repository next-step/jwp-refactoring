package kitchenpos.order.domain.fixture;

import kitchenpos.common.Quantity;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

import java.util.Collections;

public class OrderLineItemsFixture {

    public static OrderLineItems orderLineItemsA() {
        return new OrderLineItems(Collections.singletonList(new OrderLineItem(null, 1L, new Quantity(1))));
    }
}

package kitchenpos.order.domain.fixture;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

import java.util.Collections;

public class OrderLineItemsFixture {

    public static OrderLineItems orderLineItemsA() {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addAll(Collections.singletonList(new OrderLineItem(null, 1L, 1)));
        return orderLineItems;
    }
}

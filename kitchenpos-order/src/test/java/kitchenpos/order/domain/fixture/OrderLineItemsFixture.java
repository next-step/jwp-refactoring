package kitchenpos.order.domain.fixture;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.Quantity;

import java.util.Collections;
import java.util.List;

public class OrderLineItemsFixture {

    public static List<OrderLineItem> orderLineItemsA() {
        return new OrderLineItems(Collections.singletonList(new OrderLineItem(null, OrderMenuFixture.orderMenu(), new Quantity(1)))).getOrderLineItems();
    }
}

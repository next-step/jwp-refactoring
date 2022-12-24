package kitchenpos.order.domain.fixture;

import kitchenpos.common.vo.Quantity;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

import java.util.Collections;
import java.util.List;

public class OrderLineItemsFixture {

    public static List<OrderLineItem> orderLineItemsA() {
        return new OrderLineItems(Collections.singletonList(new OrderLineItem(null, OrderMenuFixture.orderMenu(), new Quantity(1)))).getOrderLineItems();
    }
}

package kitchenpos.order.domain.fixture;

import kitchenpos.common.Quantity;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;

import java.util.Collections;

public class OrderLineItemsFixture {

    public static OrderLineItems orderLineItemsA(OrderMenu orderMenu) {
        return new OrderLineItems(Collections.singletonList(new OrderLineItem(null, orderMenu, new Quantity(1))));
    }
}

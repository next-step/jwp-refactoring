package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemTestFixture {

    public static OrderLineItem createOrderLineItem(Order order, Long menuId, long quantity) {
        return OrderLineItem.of(order, menuId, quantity);
    }
}

package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemTestFixture {

    public static OrderLineItem createOrderLineItem(Long seq, Order order, Long menuId, long quantity) {
        return OrderLineItem.of(seq, order, menuId, quantity);
    }
}

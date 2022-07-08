package kitchenpos.utils.fixture;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixtureFactory {
    public static OrderLineItem createOrderLineItem(Long menuId, int quantity) {
        return OrderLineItem.of(menuId, quantity);
    }

    public static OrderLineItem createOrderLineItem(Long seq, Long menuId, int quantity) {
        return OrderLineItem.of(seq, menuId, quantity);
    }
}

package kitchenpos.factory;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixtureFactory {
    public static OrderLineItem createOrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }

    public static OrderLineItem createOrderLineItem(Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(orderId, menuId, quantity);
    }
}

package kitchenpos.order.domain;

import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemTestFixture {

    public static OrderLineItem generateOrderLineItem(OrderMenu orderMenu, long quantity) {
        return new OrderLineItem(null, null, orderMenu, quantity);
    }

    public static OrderLineItemRequest generateOrderLineItemRequest(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}

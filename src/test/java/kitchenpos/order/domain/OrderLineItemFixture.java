package kitchenpos.order.domain;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemFixture {
    private OrderLineItemFixture() {
    }

    public static OrderLineItemRequest orderLineItemRequest(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public static OrderLineItem generateOrderLineItem(long menuId, long quantity) {
        return new OrderLineItem(null, menuId, quantity);
    }

    public static OrderLineItem savedOrderLineItem(Long seq) {
        return new OrderLineItem(seq, 1L, 5);
    }
}

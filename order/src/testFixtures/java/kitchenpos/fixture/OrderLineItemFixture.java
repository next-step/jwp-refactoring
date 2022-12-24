package kitchenpos.fixture;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemFixture {
    private OrderLineItemFixture() {
    }

    public static OrderLineItemRequest orderLineItemRequest(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public static OrderLineItem savedOrderLineItem(Long seq) {
        return OrderLineItem.of(seq, 1L, 5);
    }

    public static OrderLineItem savedOrderLineItem(Long seq, Long orderMenuId, long quantity) {
        return OrderLineItem.of(seq, orderMenuId, quantity);
    }
}

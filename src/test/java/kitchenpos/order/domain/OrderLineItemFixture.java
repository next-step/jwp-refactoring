package kitchenpos.order.domain;

import java.math.BigDecimal;

import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemFixture {
    private OrderLineItemFixture() {
    }

    public static OrderLineItemRequest orderLineItemRequest(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public static OrderLineItem savedOrderLineItem(Long seq) {
        return new OrderLineItem(seq, 1L, "orderMenuName", BigDecimal.valueOf(1000), 5);
    }

    public static OrderLineItem savedOrderLineItem(Long seq, String menuName, long menuPrice) {
        return new OrderLineItem(seq, 1L, menuName, BigDecimal.valueOf(menuPrice), 5);
    }
}

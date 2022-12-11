package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemTestFixture {

    public static OrderLineItem generateOrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        return OrderLineItem.of(seq, order, menu, quantity);
    }

    public static OrderLineItem generateOrderLineItem(Menu menu, long quantity) {
        return OrderLineItem.of(menu, quantity);
    }

    public static OrderLineItemRequest generateOrderLineItemRequest(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}

package kitchenpos.fixture;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixtureFactory {
    private OrderLineItemFixtureFactory() {
    }

    public static OrderLineItem createOrderLine(Long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}

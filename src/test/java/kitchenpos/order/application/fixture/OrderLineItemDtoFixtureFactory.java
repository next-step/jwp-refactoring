package kitchenpos.order.application.fixture;

import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemDtoFixtureFactory {
    private OrderLineItemDtoFixtureFactory() {
    }

    public static OrderLineItemRequest createOrderLineItem(Long menuId, int quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}

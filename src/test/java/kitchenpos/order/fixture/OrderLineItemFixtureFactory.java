package kitchenpos.order.fixture;

import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemFixtureFactory {
    private OrderLineItemFixtureFactory() {
    }

    public static OrderLineItemRequest createOrderLine(Long menuId, int quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}

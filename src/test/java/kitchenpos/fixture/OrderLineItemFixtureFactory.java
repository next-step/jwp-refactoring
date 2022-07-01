package kitchenpos.fixture;

import kitchenpos.order.dto.OrderLineItemDto;

public class OrderLineItemFixtureFactory {
    private OrderLineItemFixtureFactory() {
    }

    public static OrderLineItemDto createOrderLine(Long menuId, int quantity) {
        return new OrderLineItemDto(menuId, quantity);
    }
}

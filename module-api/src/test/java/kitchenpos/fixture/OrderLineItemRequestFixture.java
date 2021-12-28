package kitchenpos.fixture;

import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemRequestFixture {
    public static OrderLineItemRequest 생성_Request(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}

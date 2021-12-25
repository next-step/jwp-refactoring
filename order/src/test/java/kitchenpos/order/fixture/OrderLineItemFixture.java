package kitchenpos.order.fixture;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {
    private OrderLineItemFixture() {
        throw new UnsupportedOperationException();
    }

    public static OrderLineItem create(Long seq, Long menuId, Long quantity) {
        return OrderLineItem.of(seq, menuId, quantity);
    }
}

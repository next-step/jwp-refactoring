package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemTestFixture {

    public static OrderLineItem createOrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        return OrderLineItem.of(seq, orderId, menuId, quantity);
    }
}

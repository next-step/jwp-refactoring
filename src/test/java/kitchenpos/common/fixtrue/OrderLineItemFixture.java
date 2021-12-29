package kitchenpos.common.fixtrue;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {

    private OrderLineItemFixture() {

    }

    public static OrderLineItem of(Long menuId, long quantity) {
        return OrderLineItem.of(menuId, quantity);
    }

}

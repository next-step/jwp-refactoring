package kitchenpos.common.fixtrue;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {

    private OrderLineItemFixture() {

    }

    public static OrderLineItem of(Menu menu, long quantity) {
        return OrderLineItem.of(menu, quantity);
    }

}

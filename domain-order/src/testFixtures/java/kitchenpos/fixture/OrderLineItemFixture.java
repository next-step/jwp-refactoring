package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    private OrderLineItemFixture() {

    }

    public static OrderLineItem of(Menu menu, long quantity) {
        return OrderLineItem.of(menu, quantity);
    }

}

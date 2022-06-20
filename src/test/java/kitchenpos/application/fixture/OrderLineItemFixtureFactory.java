package kitchenpos.application.fixture;


import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemFixtureFactory {

    private OrderLineItemFixtureFactory() {}

    public static OrderLineItem create(Menu menu, long quantity) {
        return OrderLineItem.of(menu, quantity);
    }
}

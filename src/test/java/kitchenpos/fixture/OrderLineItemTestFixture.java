package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemTestFixture {
    public static OrderLineItem 생성(Order order, Menu menu, long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }
}

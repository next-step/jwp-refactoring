package kitchenpos.order.domain.fixture;

import java.math.BigDecimal;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineMenu;

public class OrderLineItemFixtureFactory {
    private OrderLineItemFixtureFactory() {
    }

    public static OrderLineItem createOrderLineItem(Long menuId, String menuName, int menuPrice, int quantity) {
        OrderLineMenu orderLineMenu = new OrderLineMenu(menuId, menuName, BigDecimal.valueOf(menuPrice));
        return new OrderLineItem(orderLineMenu, quantity);
    }
}

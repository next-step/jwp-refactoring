package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문_항목(Long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static OrderLineItem 주문_항목(Menu menu, int quantity) {
        return 주문_항목(menu.getId(), quantity);
    }
}

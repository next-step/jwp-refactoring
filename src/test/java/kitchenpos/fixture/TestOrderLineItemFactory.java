package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class TestOrderLineItemFactory {
    public static OrderLineItem create(Order order, Menu menu, long quantity) {
        return create(null, order, menu, quantity);
    }

    public static OrderLineItem create(Long seq, Order order, Menu menu, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();

        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(order.getId());
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}

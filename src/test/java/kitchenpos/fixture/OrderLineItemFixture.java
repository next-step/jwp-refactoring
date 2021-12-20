package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemFixture {
    public static OrderLineItem 생성(Order order, Menu menu, long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }

    public static OrderLineItemRequest request생성(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}

package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemFixture {
    public static OrderLineItem 생성(Long menuId, long quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    public static OrderLineItem 샘플(){
        return null;
    }

    public static OrderLineItemRequest 생성_Request(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

}

package kitchenpos.domain.order.fixture;

import kitchenpos.domain.menu.domain.Menu;
import kitchenpos.domain.order.domain.OrderLineItem;
import kitchenpos.domain.order.dto.OrderLineItemRequest;

public class OrderLineItemFixture {

    public static OrderLineItem 주문_항목(Menu menu, int quantity) {
        return new OrderLineItem(menu.getId(), quantity);
    }

    public static OrderLineItemRequest 주문_항목_요청(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public static OrderLineItemRequest 주문_항목_요청(Menu menu, long quantity) {
        return 주문_항목_요청(menu.getId(), quantity);
    }
}

package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.OrderLineItemRequest;

public class OrderLineItemFixture {

    public static OrderLineItem 주문_항목(Menu menu, int quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public static OrderLineItemRequest 주문_항목_요청(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public static OrderLineItemRequest 주문_항목_요청(Menu menu, long quantity) {
        return 주문_항목_요청(menu.getId(), quantity);
    }
}

package kitchenpos.domain.order.fixture;

import kitchenpos.domain.order.domain.OrderLineItem;
import kitchenpos.domain.order.dto.OrderLineItemRequest;

public class OrderLineItemFixture {

    public static OrderLineItem 주문_항목(Long menuId, int quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    public static OrderLineItemRequest 주문_항목_요청(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}

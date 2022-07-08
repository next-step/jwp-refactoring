package kitchenpos.order.__fixture__;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.request.OrderLineItemRequest;

public class OrderLineItemTestFixture {
    public static OrderLineItem 주문_항목_생성(final Long id, final Long quantity) {
        return new OrderLineItem(id, quantity);
    }

    public static OrderLineItemRequest 주문_항목_요청_생성(final Long id, final Long quantity) {
        return new OrderLineItemRequest(id, quantity);
    }
}

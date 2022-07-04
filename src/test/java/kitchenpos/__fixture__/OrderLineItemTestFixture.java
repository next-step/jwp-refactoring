package kitchenpos.__fixture__;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemTestFixture {
    public static OrderLineItem 주문_항목_생성(final Long id, final Long quantity) {
        return new OrderLineItem(id, quantity);
    }
}

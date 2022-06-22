package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문항목_데이터_생성() {
        return new OrderLineItem(1L, 1L, 1L, 1);
    }

}

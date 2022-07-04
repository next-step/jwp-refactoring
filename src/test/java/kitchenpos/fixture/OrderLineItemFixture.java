package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문내역_데이터_생성(Long seq, Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }

}

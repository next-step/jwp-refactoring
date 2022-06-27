package kitchenpos.factory;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

public class OrderFixture {
    public static OrderTable 주문테이블_생성(Long id) {
        return new OrderTable(id, 2, false);
    }

    public static OrderLineItem 주문_메뉴_생성(Long seq, Long orderId, Long menuId) {
        return new OrderLineItem(seq, orderId, menuId, 1);
    }
}

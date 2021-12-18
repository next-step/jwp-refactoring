package kitchenpos.application.fixture;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixture {

    private OrderFixture() {
    }

    public static Order 주문_생성(Long id, List<OrderLineItem> orderLineItems) {
        Order 주문 = new Order();
        주문.setOrderLineItems(orderLineItems);
        주문.setOrderTableId(id);
        return 주문;
    }
}

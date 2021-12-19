package kitchenpos.application.fixture;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    private OrderFixture() {
    }

    public static Order 주문_생성(Long id, List<OrderLineItem> orderLineItems) {
        Order 주문 = new Order();
        주문.setOrderLineItems(orderLineItems);
        주문.setOrderTableId(id);
        return 주문;
    }


    public static Order 주문_생성(Long id, Long orderTableId, OrderStatus orderStatus,
        List<OrderLineItem> orderLineItems) {
        Order 주문 = new Order();
        주문.setId(id);
        주문.setOrderTableId(orderTableId);
        주문.setOrderStatus(orderStatus.name());
        주문.setOrderLineItems(orderLineItems);
        return 주문;
    }
}

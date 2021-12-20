package kitchenpos.application.fixture;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;

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

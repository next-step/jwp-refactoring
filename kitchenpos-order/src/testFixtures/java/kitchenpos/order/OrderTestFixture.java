package kitchenpos.order;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;

class OrderTestFixture {
    public static Order 주문_생성(Long id, Long orderTableId, OrderLineItems orderLineItems) {
        return new Order(id, orderTableId, orderLineItems);
    }

    public static Order 주문_생성(Long id, Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderLineItems);
    }
}

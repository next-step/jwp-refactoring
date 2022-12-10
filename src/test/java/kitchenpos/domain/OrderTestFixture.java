package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class OrderTestFixture {
    public static Order order(Long id, Long orderTableId, List<OrderLineItem> orderLineItems, String orderStatus) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }

    public static Order changeOrderStatusRequest(String orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus);
        return order;
    }
}

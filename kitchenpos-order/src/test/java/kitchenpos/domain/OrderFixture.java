package kitchenpos.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class OrderFixture {

    public static Order 주문(Long id, String orderStatus, Long orderTableId) {
        Order order = new Order(orderStatus, LocalDateTime.now(), orderTableId);
        ReflectionTestUtils.setField(order, "id", id);
        return order;
    }
}

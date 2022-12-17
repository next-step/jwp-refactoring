package kitchenpos.domain;

import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {

    public static Order 주문(Long id, Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems) {
        Order order = new Order(orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
        ReflectionTestUtils.setField(order, "id", id);
        return order;
    }
}

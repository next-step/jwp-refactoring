package kitchenpos.order.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {

    public static Order 주문(Long id, String orderStatus, OrderTable orderTable) {
        Order order = new Order(orderStatus, LocalDateTime.now(), orderTable);
        ReflectionTestUtils.setField(order, "id", id);
        return order;
    }
}

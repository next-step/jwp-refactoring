package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

public class OrderTest {

    public static Order 주문_생성(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order.Builder()
                .id(id)
                .orderTable(orderTable)
                .orderStatus(orderStatus)
                .orderedTime(orderedTime)
                .orderLineItems(orderLineItems)
                .build();
    }
}
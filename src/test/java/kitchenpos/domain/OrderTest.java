package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
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
package kitchenpos.order.domain;

import java.util.List;

public class OrderTest {

    public static Order 주문_생성(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order.Builder()
                .id(id)
                .orderTable(orderTable)
                .orderLineItems(orderLineItems)
                .build();
    }
}
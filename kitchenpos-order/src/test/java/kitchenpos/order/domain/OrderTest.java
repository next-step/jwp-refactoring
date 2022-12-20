package kitchenpos.order.domain;

import java.util.List;

public class OrderTest {

    public static Order 주문_생성(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order.Builder()
                .id(id)
                .orderTableId(orderTableId)
                .orderLineItems(orderLineItems)
                .build();
    }
}
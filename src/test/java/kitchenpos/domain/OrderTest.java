package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class OrderTest {

    public static Order 주문_생성(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order.Builder()
                .id(id)
                .orderTableId(orderTableId)
                .orderStatus(orderStatus)
                .orderedTime(orderedTime)
                .orderLineItems(orderLineItems)
                .build();
    }
}
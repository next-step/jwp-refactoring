package kitchenpos.helper;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderFixtureHelper {

    public static Order 주문_만들기(OrderStatus orderStatus, OrderLineItem orderLineItem, Long orderTableId) {
        return OrderBuilder.builder()
                .orderTableId(orderTableId)
                .orderedTime(LocalDateTime.now())
                .orderStatus(orderStatus)
                .orderLineItems(Arrays.asList(orderLineItem)).build();
    }

}

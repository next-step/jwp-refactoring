package kitchenpos.application.sample;

import static kitchenpos.application.sample.OrderLineItemSample.twoOrderItem;

import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderSample {

    public static Order cookingOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(Collections.singletonList(twoOrderItem()));
        return order;
    }

    public static Order completedOrder() {
        Order order = new Order();
        order.setId(2L);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(Collections.singletonList(twoOrderItem()));
        return order;
    }

}

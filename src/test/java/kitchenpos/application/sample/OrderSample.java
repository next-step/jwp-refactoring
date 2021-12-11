package kitchenpos.application.sample;

import static kitchenpos.application.sample.OrderLineItemSample.후라이드치킨세트_두개;
import static kitchenpos.application.sample.OrderTableSample.채워진_다섯명_테이블;

import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderSample {

    public static Order 조리중인_후라이트치킨세트_두개_주문() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(채워진_다섯명_테이블().getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(Collections.singletonList(후라이드치킨세트_두개()));
        return order;
    }

    public static Order 완료된_후라이트치킨세트_두개_주문() {
        Order order = new Order();
        order.setId(2L);
        order.setOrderTableId(채워진_다섯명_테이블().getId());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(Collections.singletonList(후라이드치킨세트_두개()));
        return order;
    }
}

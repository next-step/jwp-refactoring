package kitchenpos.order.application.sample;

import static kitchenpos.order.application.sample.OrderLineItemSample.후라이드치킨세트_두개;
import static kitchenpos.table.application.sample.OrderTableSample.채워진_다섯명_테이블;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Collections;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderSample {

    public static Order 조리중인_후라이트치킨세트_두개_주문() {
        Order order = spy(Order.of(
            채워진_다섯명_테이블(), Collections.singletonList(후라이드치킨세트_두개())));
        when(order.id()).thenReturn(1L);
        return order;
    }

    public static Order 완료된_후라이트치킨세트_두개_주문() {
        Order order = spy(Order.of(
            채워진_다섯명_테이블(), Collections.singletonList(후라이드치킨세트_두개())));
        order.changeStatus(OrderStatus.COMPLETION);
        when(order.id()).thenReturn(2L);
        return order;
    }
}

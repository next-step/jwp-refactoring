package kichenpos.order.order.sample;

import static kichenpos.order.order.sample.OrderLineItemSample.이십원_후라이트치킨_두마리세트_한개_주문_항목;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import java.util.Collections;
import kichenpos.order.order.domain.Order;
import kichenpos.order.order.domain.OrderStatus;

public class OrderSample {

    public static Order 조리중인_후라이트치킨세트_두개_주문() {
        Order order = spy(Order.of(1L,
            Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목())));

        lenient().when(order.id())
            .thenReturn(1L);
        return order;
    }

    public static Order 완료된_후라이트치킨세트_두개_주문() {
        Order order = spy(Order.of(1L,
            Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목())));
        order.changeStatus(OrderStatus.COMPLETION);

        lenient().when(order.id())
            .thenReturn(2L);
        return order;
    }
}

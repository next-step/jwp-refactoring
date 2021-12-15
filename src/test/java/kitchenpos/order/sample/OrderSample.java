package kitchenpos.order.sample;

import static kitchenpos.order.sample.OrderLineItemSample.이십원_후라이트치킨_두마리세트_한개_주문_항목;
import static kitchenpos.order.sample.OrderTableSample.채워진_다섯명_테이블;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import java.util.Collections;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderSample {

    public static Order 조리중인_후라이트치킨세트_두개_주문() {
        Order order = spy(Order.of(
            채워진_다섯명_테이블(),
            Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목())));
        lenient().when(order.id())
            .thenReturn(1L);
        return order;
    }

    public static Order 완료된_후라이트치킨세트_두개_주문() {
        Order order = spy(Order.of(
            채워진_다섯명_테이블(),
            Collections.singletonList(이십원_후라이트치킨_두마리세트_한개_주문_항목())));
        order.changeStatus(OrderStatus.COMPLETION);
        lenient().when(order.id())
            .thenReturn(2L);
        return order;
    }
}

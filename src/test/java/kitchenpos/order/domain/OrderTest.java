package kitchenpos.order.domain;

import java.time.LocalDateTime;

import static kitchenpos.order.domain.OrderLineItemTest.와퍼_세트_주문;
import static kitchenpos.order.domain.OrderLineItemTest.콜라_주문;
import static kitchenpos.table.domain.OrderTableTest.이인석;

public class OrderTest {
    public static final Order 주문통합 = 주문생성();

    private static Order 주문생성() {
        Order order = new Order(
                1L
                , 이인석
                , OrderStatus.COOKING
                , LocalDateTime.now());
        order.addOrderItem(와퍼_세트_주문);
        order.addOrderItem(콜라_주문);

        return order;
    }
}

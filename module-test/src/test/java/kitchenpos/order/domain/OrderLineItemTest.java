package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderTest.주문통합;

public class OrderLineItemTest {
    public static final OrderLineItem 와퍼_세트_주문 = new OrderLineItem(1L, 주문통합, 1L, 2);
    public static final OrderLineItem 콜라_주문 = new OrderLineItem(2L, 주문통합, 2L, 1);
}

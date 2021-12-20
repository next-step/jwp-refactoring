package kitchenpos.domain;

import kitchenpos.order.domain.OrderLineItem;

import static kitchenpos.menu.domain.MenuTest.양념치킨_단품;
import static kitchenpos.menu.domain.MenuTest.치킨세트;
import static kitchenpos.domain.OrderTest.주문통합;

public class OrderLineItemTest {
    public static final OrderLineItem 와퍼_세트_주문 = new OrderLineItem(1L, 주문통합, 치킨세트, 2);
    public static final OrderLineItem 콜라_주문 = new OrderLineItem(2L, 주문통합, 양념치킨_단품, 1);
}

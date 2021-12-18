package kitchenpos.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.domain.OrderLineItemTest.와퍼_세트_주문;
import static kitchenpos.domain.OrderLineItemTest.콜라_주문;
import static kitchenpos.domain.OrderTableTest.이인석;

public class OrderTest {
    public static final Order 주문통합 = new Order(1L, 이인석, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(와퍼_세트_주문, 콜라_주문));
}

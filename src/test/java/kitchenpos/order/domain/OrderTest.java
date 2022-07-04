package kitchenpos.order.domain;

import kitchenpos.common.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {
    Quantity quantity;
    OrderLineItem orderLineItem;
    Order order;

    @BeforeEach
    void setUp() {
        quantity = new Quantity(10L);
        orderLineItem = new OrderLineItem(new Order(), 1L, quantity.value());
        order = new Order(1L, 1L, OrderStatus.COOKING, null, new OrderLineItems(Collections.singletonList(orderLineItem)));
    }

    @Test
    void changeOrderStatus() {
        // when
        order.changeOrderStatus(MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(MEAL);
    }
}

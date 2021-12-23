package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTest {

    @Test
    @DisplayName("주문을 등록한다")
    void createOrderTest() {

        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 10);
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(orderLineItem));

        assertAll(() -> assertThat(order.getId()).isEqualTo(1L),
                () -> assertThat(order.getOrderTableId()).isEqualTo(1L),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(order.getOrderLineItems()).contains(orderLineItem)
        );
    }

    @Test
    @DisplayName("주문상태를 변경한다")
    void modifyOrderTest() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 10);
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),Arrays.asList(orderLineItem));
        Order changedOrder = Order.of(order.getId(), order.getOrderTableId(), OrderStatus.MEAL.name(), LocalDateTime.now(), order.getOrderLineItems());
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

}

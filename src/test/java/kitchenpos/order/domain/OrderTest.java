package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @DisplayName("주문에 주문항목을 추가한다.")
    @Test
    void add() {

        Order order = Order.builder()
                .id(1L)
                .orderLineItems(new OrderLineItems())
                .orderedTime(LocalDateTime.now())
                .orderStatus(OrderStatus.MEAL)
                .build();

        OrderLineItem orderLineItem = OrderLineItem.builder()
                .id(1L)
                .order(new Order())
                .quantity(5L)
                .build();

        order.addOrderLineItem(orderLineItem);

        assertThat(order.getOrderLineItems()).isNotEmpty();
    }
}

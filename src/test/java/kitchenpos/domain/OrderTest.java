package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class OrderTest {
    @Test
    void 생성() {
        Order order = Order.of(1L, OrderStatus.MEAL, Arrays.asList(new OrderLineItem()));

        assertAll(
                () -> assertThat(order.getOrderTableId()).isEqualTo(1L),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL),
                () -> assertThat(order.getOrderLineItems().size()).isEqualTo(1)
        );
    }
}

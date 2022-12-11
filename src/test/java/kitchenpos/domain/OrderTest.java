package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class OrderTest {
    @Test
    void 생성() {
        LocalDateTime NOW = LocalDateTime.now();
        Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);
        order.setOrderStatus("MEAL");
        order.setOrderedTime(NOW);
        order.setOrderLineItems(Arrays.asList(new OrderLineItem()));

        assertAll(
                () -> assertThat(order.getId()).isEqualTo(1L),
                () -> assertThat(order.getOrderTableId()).isEqualTo(1L),
                () -> assertThat(order.getOrderStatus()).isEqualTo("MEAL"),
                () -> assertThat(order.getOrderedTime()).isEqualTo(NOW),
                () -> assertThat(order.getOrderLineItems().size()).isEqualTo(1)
        );
    }
}

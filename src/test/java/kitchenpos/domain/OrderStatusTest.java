package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    void completionTest() {

        OrderStatus orderStatus = OrderStatus.MEAL;

        assertThat(orderStatus.isNotCompletion()).isTrue();
    }
}

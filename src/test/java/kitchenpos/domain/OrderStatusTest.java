package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusTest {

    @Test
    void onGoingOrderStatus() {
        assertThat(OrderStatus.onGoingOrderStatus()).containsExactly(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
    }
}

package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusTest {

    @Test
    void isComplete_완료_상태의_주문_상태인지_확인한다() {
        assertThat(OrderStatus.COMPLETION.isComplete()).isTrue();
        assertThat(OrderStatus.COOKING.isComplete()).isFalse();
        assertThat(OrderStatus.MEAL.isComplete()).isFalse();
    }
}
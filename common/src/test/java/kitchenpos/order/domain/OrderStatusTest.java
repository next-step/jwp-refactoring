package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @DisplayName("완료 상태인지 확인")
    @Test
    void isCompletion() {
        assertAll(
            () -> assertThat(OrderStatus.COMPLETION.isCompletion()).isTrue(),
            () -> assertThat(OrderStatus.COOKING.isCompletion()).isFalse(),
            () -> assertThat(OrderStatus.MEAL.isCompletion()).isFalse()
        );
    }
}
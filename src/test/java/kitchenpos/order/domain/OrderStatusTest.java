package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderStatusTest {
    @Test
    @DisplayName("주문상태 계산 완료이면 true 반환하는지 확인")
    void isCompletion() {
        // given
        OrderStatus orderStatus = OrderStatus.COMPLETION;

        // then
        assertThat(orderStatus.isCompletion()).isTrue();
    }
}

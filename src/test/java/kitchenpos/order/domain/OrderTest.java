package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @DisplayName("계산 완료 상태인 주문 상태변경 시 예외처리")
    @Test
    void 계산_완료_주문_상태변경_예외처리() {
        Order 계산_완료된_주문 = new Order(1L, OrderStatus.COMPLETION);

        assertThatThrownBy(
                () -> 계산_완료된_주문.updateOrderStatus(OrderStatus.COOKING)).isInstanceOf(
                IllegalArgumentException.class);
    }
}

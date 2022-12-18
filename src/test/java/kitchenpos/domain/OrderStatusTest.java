package kitchenpos.domain;

import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusTest {

    @DisplayName("주문정보의 COOKING, MEAL 상태를 가져오는 작업을 성공한다.")
    @Test
    void onGoingOrderStatus() {
        assertThat(OrderStatus.onGoingOrderStatus()).containsExactly(OrderStatus.COOKING, OrderStatus.MEAL);
    }
}

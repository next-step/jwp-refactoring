package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

class OrderTest {
    @Test
    void changeOrderStatus() {
        // given
        final Order order = new Order(1L, MEAL, Collections.singletonList(new OrderLineItem(1L, 1L)));

        // when
        order.changeOrderStatus(COOKING);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(COOKING);
    }

    @DisplayName("계산 완료(COMPLETION)된 주문의 상태를 바꾸려할 때 예외 발생")
    @Test
    void changeCompletedOrderStatus() {
        // given
        final Order order = new Order(1L, COMPLETION, Collections.singletonList(new OrderLineItem(1L, 1L)));

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> order.changeOrderStatus(COOKING))
            .withMessageContaining("계산 완료된 주문의 상태 수정은 불가능합니다");
    }
}

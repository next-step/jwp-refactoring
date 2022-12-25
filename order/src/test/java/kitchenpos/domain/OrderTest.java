package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import kitchenpos.fixture.OrderFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

class OrderTest {
    @DisplayName("주문 상태 변경 - 이미 완료된 주문")
    @ParameterizedTest
    @EnumSource
    void updateOrderStatus_already_complete_failed(OrderStatus orderStatus) {
        // given
        Order order = OrderFixture.savedOrder(1L, OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> order.updateOrderStatus(orderStatus))
            .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("주문 상태 변경")
    @ParameterizedTest
    @EnumSource
    void updateOrderStatus(OrderStatus orderStatus) {
        // given
        Order order = OrderFixture.savedOrder(1L, OrderStatus.COOKING);

        // when
        order.updateOrderStatus(orderStatus);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }
}

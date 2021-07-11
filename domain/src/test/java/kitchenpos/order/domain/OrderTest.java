package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.error.InvalidOrderStatusException;
import kitchenpos.error.OrderStatusException;

@DisplayName("주문 도메인 테스트")
class OrderTest {

    @DisplayName("생성")
    @Test
    void create() {
        // given
        // when
        Order order = Order.of(1L, OrderStatus.MEAL);
        // then
        assertThat(order).isNotNull();
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        // given
        Order order = Order.of(1L, OrderStatus.MEAL);
        // when
        order.changeOrderStatus(OrderStatus.COMPLETION);
        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("주문 상태 체크 - COOKING, MEAL인지")
    @Test
    void checkChangeableStatus() {
        // given
        Order order = Order.of(1L, OrderStatus.MEAL);
        // when
        // then
        assertThatThrownBy(order::checkChangeableStatus)
                .isInstanceOf(InvalidOrderStatusException.class);
    }

    @DisplayName("주문 상태 체크 - COMPLETE 인지")
    @Test
    void checkAlreadyComplete() {
        // given
        Order order = Order.of(1L, OrderStatus.COMPLETION);
        // when
        // then
        assertThatThrownBy(order::checkAlreadyComplete)
                .isInstanceOf(OrderStatusException.class);
    }
}
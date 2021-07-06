package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
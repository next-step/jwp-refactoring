package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 단위테스트")
class OrderTest {
    @DisplayName("주문항목이 있는 지 확인")
    @Test
    void isEmptyItem() {
        Order order = new Order(1L);
        order.addOrderLineItem(new OrderLineItem(1L, 1));
        assertThat(order.isEmptyItem()).isFalse();
    }

    @DisplayName("주문의 주문 상태를 변경할 수 있다")
    @Test
    void changeStatus() {
        Order order = new Order(1L);
        OrderStatus status = OrderStatus.MEAL;
        order.changeStatus(status);
        assertThat(order.getOrderStatus()).isEqualTo(status);
    }

    @DisplayName("주문 상태가 계산 완료 인 주문은 상태를 변경할 수 없다")
    @Test
    void completion_order_cannot_change() {
        Order order = new Order(1L, OrderStatus.COMPLETION);
        OrderStatus status = OrderStatus.MEAL;
        assertThatThrownBy(() -> order.changeStatus(status))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

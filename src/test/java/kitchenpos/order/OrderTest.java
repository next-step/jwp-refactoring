package kitchenpos.order;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 관련 기능 테스트")
public class OrderTest {

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        Order order = new Order(1L);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("메뉴를 주문한다.")
    void order() {
        Order order = new Order(1L);
        order.order(1L, 1L);
        assertThat(order.getOrderLineItems().getOrderLineItems()).hasSize(1);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeStatus() {
        Order order = new Order(1L);
        order.changeOrderStatus(OrderStatus.MEAL.name());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    @DisplayName("완료 상태인 주문의 상태를 변경하면 실패한다.")
    void changeStatusOfCompletedOrder() {
        Order order = new Order(1L);
        order.changeOrderStatus(OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("완료된 주문은 상태를 변경할 수 없습니다");
    }

    @Test
    void changeStatusToInvalidStatus() {
        Order order = new Order(1L);

        assertThatThrownBy(() -> order.changeOrderStatus("status"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 상태가 올바르지 않습니다");
    }
}

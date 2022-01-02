package kitchenpos.order.domain;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.EmptyOrderStatusException;
import kitchenpos.order.exception.NotOrderStatusCompleteException;
import kitchenpos.order.exception.NotOrderStatusException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 상태 객체의 대한 단위 테스트")
class OrderStatusTest {
    @DisplayName("변경하려는 주문의 상태가 값이 없으면 변경할 수 없다.")
    @Test
    void changeNull() {
        final OrderStatus orderStatus = OrderStatus.COOKING;

        Assertions.assertThatThrownBy(() -> orderStatus.validateStatus(null))
                .isInstanceOf(EmptyOrderStatusException.class);
    }

    @DisplayName("주문의 상태가 Cooking일때는 Complete로 변경할 수 없다.")
    @Test
    void changePostStatus() {
        final OrderStatus orderStatus = OrderStatus.COOKING;

        Assertions.assertThatThrownBy(() -> orderStatus.validateStatus(OrderStatus.COMPLETION))
                .isInstanceOf(NotOrderStatusException.class);
    }

    @DisplayName("주문의 상태가 Meal일때는 Cooking으로 변경할 수 없다.")
    @Test
    void changePreStatus() {
        final OrderStatus orderStatus = OrderStatus.MEAL;

        Assertions.assertThatThrownBy(() -> orderStatus.validateStatus(OrderStatus.COOKING))
                .isInstanceOf(NotOrderStatusException.class);
    }

    @DisplayName("주문의 Completeion일때는 변경할 수 없다.")
    @Test
    void changeNotCompletion() {
        final OrderStatus orderStatus = OrderStatus.COMPLETION;

        Assertions.assertThatThrownBy(() -> orderStatus.validateStatus(OrderStatus.COOKING))
                .isInstanceOf(NotOrderStatusCompleteException.class);
    }
}

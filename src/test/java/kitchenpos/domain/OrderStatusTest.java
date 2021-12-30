package kitchenpos.domain;

import kitchenpos.common.exceptions.EmptyOrderStatusException;
import kitchenpos.common.exceptions.NotOrderStatusCompleteException;
import kitchenpos.common.exceptions.NotOrderStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 상태 객체의 대한 단위 테스트")
class OrderStatusTest {
    @DisplayName("변경하려는 주문의 상태가 값이 없으면 변경할 수 없다.")
    @Test
    void changeNull() {
        final OrderStatus orderStatus = OrderStatus.COOKING;

        assertThatThrownBy(() -> orderStatus.validateStatus(null))
                .isInstanceOf(EmptyOrderStatusException.class);
    }

    @DisplayName("주문의 상태가 Cooking일때는 Complete로 변경할 수 없다.")
    @Test
    void changePostStatus() {
        final OrderStatus orderStatus = OrderStatus.COOKING;

        assertThatThrownBy(() -> orderStatus.validateStatus(OrderStatus.COMPLETION))
                .isInstanceOf(NotOrderStatusException.class);
    }

    @DisplayName("주문의 상태가 Meal일때는 Cooking으로 변경할 수 없다.")
    @Test
    void changePreStatus() {
        final OrderStatus orderStatus = OrderStatus.MEAL;

        assertThatThrownBy(() -> orderStatus.validateStatus(OrderStatus.COOKING))
                .isInstanceOf(NotOrderStatusException.class);
    }

    @DisplayName("주문의 Completeion일때는 변경할 수 없다.")
    @Test
    void changeNotCompletion() {
        final OrderStatus orderStatus = OrderStatus.COMPLETION;

        assertThatThrownBy(() -> orderStatus.validateStatus(OrderStatus.COOKING))
                .isInstanceOf(NotOrderStatusCompleteException.class);
    }
}

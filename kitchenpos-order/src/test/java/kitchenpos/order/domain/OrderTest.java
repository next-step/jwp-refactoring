package kitchenpos.order.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.fixture.TestOrderFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {
    @DisplayName("계산완료 상태에 대한 유효성을 확인할 수 있다.")
    @Test
    void statusShouldCompleteException() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = TestOrderFactory.create(orderTable.getId(), OrderStatus.MEAL, new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> order.validateOrderStatusShouldComplete())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_STATUS_NOT_COMPLETE.getMessage());
    }

    @DisplayName("주문의 상태를 수정할 수 있다.")
    @Test
    void updateOrderStatus() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = TestOrderFactory.create(orderTable.getId(), OrderStatus.COOKING, new ArrayList<>());

        // when
        order.setOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }
}

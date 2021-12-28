package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.BadRequestException;

class OrderTest {

    @DisplayName("빈 테이블은 주문을 생성할 수 없다.")
    @Test
    void createOrderEmptyOrderTable() {
        // given
        List<OrderLineItem> orderLineItems = Collections.emptyList();

        // when && then
        assertThatThrownBy(() -> Order.of(null, orderLineItems))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(WRONG_VALUE.getMessage());
    }

    @DisplayName("주문 상태 갱신은 계산 완료가 아니어야 가능하다.")
    @Test
    void changeOrderStatusCompletion() {
        // given
        OrderTable orderTable = OrderTable.of(1L, null, 0, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            OrderLineItem.of(1L, 1),
            OrderLineItem.of(2L, 2));
        Order order = Order.of(orderTable, orderLineItems);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when && then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(CANNOT_CHANGE_STATUS.getMessage());
    }
}

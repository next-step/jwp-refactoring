package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.ordertable.domain.OrderTable;

class OrderTest {

    @DisplayName("빈 테이블은 주문을 생성할 수 없다.")
    @Test
    void createOrderEmptyOrderTable() {
        // given
        OrderTable orderTable = OrderTable.of(0, true);
        List<OrderLineItem> orderLineItems = Collections.emptyList();

        // when && then
        assertThatThrownBy(() -> Order.of(orderTable.getId(), orderLineItems))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(WRONG_VALUE.getMessage());
    }

    @DisplayName("주문 생성시 주문 항목에 주문이 등록된다.")
    @Test
    void createOrderAddToOrderLineItems() {
        // given
        OrderTable orderTable = OrderTable.of(1L, null, 0, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            OrderLineItem.of(null, 1),
            OrderLineItem.of(null, 2));

        // when
        Order order = Order.of(orderTable.getId(), orderLineItems);

        // then
        assertThat(order.getOrderLineItems().getValue())
            .extracting("order")
            .containsExactlyElementsOf(Arrays.asList(order, order));
    }

    @DisplayName("주문 상태 갱신은 계산 완료가 아니어야 가능하다.")
    @Test
    void changeOrderStatusCompletion() {
        // given
        OrderTable orderTable = OrderTable.of(1L, null, 0, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            OrderLineItem.of(null, 1),
            OrderLineItem.of(null, 2));
        Order order = Order.of(orderTable.getId(), orderLineItems);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when && then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(CANNOT_CHANGE_STATUS.getMessage());
    }
}

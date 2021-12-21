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
        OrderTable orderTable = new OrderTable(0, true);
        List<OrderLineItem> orderLineItems = Collections.emptyList();

        // when && then
        assertThatThrownBy(() -> Order.of(orderTable, orderLineItems))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(WRONG_VALUE.getMessage());
    }

    @DisplayName("주문 생성시 주문 항목에 주문이 등록된다.")
    @Test
    void createOrderAddToOrderLineItems() {
        // given
        OrderTable orderTable = new OrderTable(0, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(
            OrderLineItem.of(null, 1),
            OrderLineItem.of(null, 2));

        // when
        Order order = Order.of(orderTable, orderLineItems);

        // then
        assertThat(order.getOrderLineItems().getValue())
            .extracting("order")
            .containsExactlyElementsOf(Arrays.asList(order, order));
    }
}

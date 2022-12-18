package kitchenpos.order.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.exception.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.utils.Message.EMPTY_ORDER_TABLE;
import static kitchenpos.utils.Message.INVALID_EMPTY_LINE_ITEMS;

@DisplayName("주문 테스트")
class OrderTest {

    @DisplayName("id가 같은 두 객체는 같다.")
    @Test
    void equalsTest() {
        OrderTable orderTable = OrderTable.of(10, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(OrderLineItem.of(1L, 2));

        Order order1 = Order.of(1L, orderTable, orderLineItems);
        Order order2 = Order.of(1L, orderTable, orderLineItems);

        Assertions.assertThat(order1).isEqualTo(order2);
    }

    @DisplayName("id가 다르면 두 객체는 다르다.")
    @Test
    void equalsTest2() {
        OrderTable orderTable = OrderTable.of(10, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(OrderLineItem.of(1L, 2));

        Order order1 = Order.of(1L, orderTable, orderLineItems);
        Order order2 = Order.of(2L, orderTable, orderLineItems);

        Assertions.assertThat(order1).isNotEqualTo(order2);
    }

    @DisplayName("주문항목이 비어있으면 주문 생성 시 예외가 발생한다.")
    @Test
    void createException() {
        OrderTable orderTable = OrderTable.of(10, false);
        List<OrderLineItem> orderLineItems = Collections.emptyList();

        Assertions.assertThatThrownBy(() -> Order.of(orderTable, orderLineItems))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_EMPTY_LINE_ITEMS);
    }

    @DisplayName("주문 테이블이 empty 이면 주문 생성 시 예외가 발생한다.")
    @Test
    void createException2() {
        OrderTable orderTable = OrderTable.of(10, true);
        List<OrderLineItem> orderLineItems = Arrays.asList(OrderLineItem.of(1L, 2));

        Assertions.assertThatThrownBy(() -> Order.of(orderTable, orderLineItems))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(EMPTY_ORDER_TABLE);
    }
}

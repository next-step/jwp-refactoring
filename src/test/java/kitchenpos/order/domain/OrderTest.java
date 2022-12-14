package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.exception.EmptyOrderLineItemException;
import kitchenpos.order.exception.EmptyOrderTableException;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테스트")
class OrderTest {

    @DisplayName("id가 같은 두 객체는 동등하다.")
    @Test
    void equalsTest() {
        OrderTable orderTable = OrderTable.of(10, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(OrderLineItem.of(1L, 2));

        Order order1 = Order.of(1L, orderTable, orderLineItems);
        Order order2 = Order.of(1L, orderTable, orderLineItems);

        Assertions.assertThat(order1).isEqualTo(order2);
    }

    @DisplayName("id가 다르면 두 객체는 동등하지 않다.")
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
                .isInstanceOf(EmptyOrderLineItemException.class)
                .hasMessageStartingWith(ExceptionMessage.EMPTY_ORDER_LINE_ITEM);
    }

    @DisplayName("주문 테이블이 empty 이면 주문 생성 시 예외가 발생한다.")
    @Test
    void createException2() {
        OrderTable orderTable = OrderTable.of(10, true);
        List<OrderLineItem> orderLineItems = Arrays.asList(OrderLineItem.of(1L, 2));

        Assertions.assertThatThrownBy(() -> Order.of(orderTable, orderLineItems))
                .isInstanceOf(EmptyOrderTableException.class)
                .hasMessageStartingWith(ExceptionMessage.EMPTY_ORDER_TABLE);
    }
}

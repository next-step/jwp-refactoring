package kitchenpos.order.domain;

import kitchenpos.exception.InvalidOrderException;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {
    @DisplayName("빈 테이블의 주문은 등록할 수 없다.")
    @Test
    void create() {
        // given
        OrderTable 빈_테이블 = OrderTable.of(1L, null, 2, true);

        //then
        assertThatThrownBy(
                () -> Order.of(1L, 빈_테이블, null, null, null)
        ).isInstanceOf(InvalidOrderException.class);
    }

    @DisplayName("주문 항목이 없는 주문은 등록할 수 없다.")
    @Test
    void create2() {
        // given
        OrderTable orderTable = OrderTable.of(1L, null, 2, false);

        // then
        assertThatThrownBy(
                () -> Order.of(1L, orderTable, null, null, new ArrayList<>())
        ).isInstanceOf(InvalidOrderException.class);
    }

    @DisplayName("계산 완료인 경우 주문 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderTable orderTable = OrderTable.of(1L, null, 2, false);
        OrderLineItem orderLineItem = OrderLineItem.of(1L, null, null, 1);
        Order 계산완료_주문 = Order.of(1L, orderTable, OrderStatus.COMPLETION, LocalDateTime.now(), Arrays.asList(orderLineItem));

        //then
        assertThatThrownBy(
                () -> 계산완료_주문.changeOrderStatus(OrderStatus.COOKING)
        ).isInstanceOf(InvalidOrderStatusException.class);
    }

    @DisplayName("식사중에서 조리중으로 주문상태를 거꾸로 변경할 수 없다.")
    @Test
    void changeOrderStatus2() {
        // given
        OrderTable orderTable = OrderTable.of(1L, null, 2, false);
        OrderLineItem orderLineItem = OrderLineItem.of(1L, null, null, 1);
        Order 식사중_주문 = Order.of(1L, orderTable, OrderStatus.MEAL, LocalDateTime.now(), Arrays.asList(orderLineItem));

        //then
        assertThatThrownBy(
                () -> 식사중_주문.changeOrderStatus(OrderStatus.COOKING)
        ).isInstanceOf(InvalidOrderStatusException.class);
    }
}

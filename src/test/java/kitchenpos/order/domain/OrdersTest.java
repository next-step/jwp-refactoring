package kitchenpos.order.domain;

import kitchenpos.order.domain.exception.CannotUngroupException;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrdersTest {

    @DisplayName("주문 테이블에 해당된 주문 중에 계산완료되지 않은 주문이 있다면 테이블 그룹을 해제할 수 없습니다.")
    @Test
    void validateAllNotCompletionStatus() {
        //given
        OrderTable orderTable = OrderTable.of(2, false);
        Order order1 = Order.createWithMapping(orderTable, OrderStatus.COMPLETION, Lists.list());
        Order order2 = Order.createWithMapping(orderTable, OrderStatus.MEAL, Lists.list());
        Order order3 = Order.createWithMapping(orderTable, OrderStatus.COMPLETION, Lists.list());
        Orders orders = Orders.of(Lists.list(order1, order2, order3));

        //when
        assertThatThrownBy(orders::validateAllNotCompletionStatus)
                .isInstanceOf(CannotUngroupException.class); //then
    }
}

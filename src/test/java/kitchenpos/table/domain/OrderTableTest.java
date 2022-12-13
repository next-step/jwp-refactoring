package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("공석여부를 수정할경우 주문이 계산완료가 아닐경우 예외발생")
    @Test
    public void throwsExceptionWhenStatusNotComplete() {
        OrderTable orderTable = OrderTable.builder().build();
        Order order = Order.builder()
                .orderTable(OrderTable.builder().build())
                .orderStatus(OrderStatus.COOKING).build();
        List<Order> orders = Arrays.asList(order);

        assertThatThrownBy(() -> orderTable.changeEmpty(true, orders))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공석여부를 수정할경우 테이블그룹에 소속되있는경우 예외발생")
    @Test
    public void throwsExceptionWhenHasTableGroup() {
        OrderTable orderTable = OrderTable.builder().tableGroup(TableGroup.builder().build()).build();
        Order order = Order.builder()
                .orderTable(OrderTable.builder().build())
                .orderStatus(OrderStatus.COMPLETION).build();
        List<Order> orders = Arrays.asList(order);

        assertThatThrownBy(() -> orderTable.changeEmpty(true, orders))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님수를 수정할경우 손님수가 음수면 예외발생")
    @Test
    public void throwsExceptionWhenNegativeGuest() {
        OrderTable orderTable = OrderTable.builder().build();

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님수를 수정할경우 테이블이 공석이면 예외발생")
    @Test
    public void throwsExceptionWhenEmptyTable() {
        OrderTable orderTable = OrderTable.builder().empty(true).build();

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

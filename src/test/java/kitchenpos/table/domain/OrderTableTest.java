package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderException;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("공석여부를 수정할경우 주문이 식사중일 경우 예외발생")
    @Test
    public void throwsExceptionWhenStatusIsMeal() {
        OrderTable orderTable = OrderTable.builder().build();
        Order order = Order.builder()
                .orderTable(OrderTable.builder().build())
                .orderStatus(OrderStatus.MEAL).build();
        List<Order> orders = Arrays.asList(order);

        assertThatThrownBy(() -> orderTable.changeEmpty(true, orders))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining("계산이 끝나지 않은 주문은 상태를 변경할 수 없습니다");
    }

    @DisplayName("공석여부를 수정할경우 주문이 조리중일 경우 예외발생")
    @Test
    public void throwsExceptionWhenStatusIsCooking() {
        OrderTable orderTable = OrderTable.builder().build();
        Order order = Order.builder()
                .orderTable(OrderTable.builder().build())
                .orderStatus(OrderStatus.COOKING).build();
        List<Order> orders = Arrays.asList(order);

        assertThatThrownBy(() -> orderTable.changeEmpty(true, orders))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining("계산이 끝나지 않은 주문은 상태를 변경할 수 없습니다");
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

    @DisplayName("공석여부를 수정할경우 값이 변경")
    @Test
    public void returnIsEmpty() {
        OrderTable orderTable = OrderTable.builder().build();
        Order order = Order.builder()
                .orderTable(OrderTable.builder().build())
                .orderStatus(OrderStatus.COMPLETION).build();
        List<Order> orders = Arrays.asList(order);

        orderTable.changeEmpty(true, orders);

        assertThat(orderTable.isEmpty()).isTrue();

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

    @DisplayName("손님수를 수정할경우 값이 변경")
    @Test
    public void returnNumberOfGuests() {
        int numberOfGuests = Arbitraries.integers().between(2,100).sample();
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(2)
                .empty(false).build();

        orderTable.changeNumberOfGuests(numberOfGuests);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}

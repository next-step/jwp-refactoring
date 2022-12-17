package kitchenpos.table.domain;

import kitchenpos.table.exception.OrderTableException;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("공석여부를 수정할경우 테이블그룹에 소속되있는경우 예외발생")
    @Test
    public void throwsExceptionWhenHasTableGroup() {
        OrderTable orderTable = OrderTable.builder().tableGroupId(1l).build();

        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(OrderTableException.class)
                .hasMessageContaining("사용중인 테이블그룹이 존재합니다");

    }

    @DisplayName("공석여부를 수정할경우 값이 변경")
    @Test
    public void returnIsEmpty() {
        OrderTable orderTable = OrderTable.builder().build();

        orderTable.changeEmpty(true);

        assertThat(orderTable.isEmpty()).isTrue();

    }

    @DisplayName("손님수를 수정할경우 손님수가 음수면 예외발생")
    @Test
    public void throwsExceptionWhenNegativeGuest() {
        OrderTable orderTable = OrderTable.builder().build();

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(OrderTableException.class)
                .hasMessageContaining("손님수는 0명이상 이어야합니다");
    }

    @DisplayName("손님수를 수정할경우 테이블이 공석이면 예외발생")
    @Test
    public void throwsExceptionWhenEmptyTable() {
        OrderTable orderTable = OrderTable.builder().empty(true).build();

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                .isInstanceOf(OrderTableException.class)
                .hasMessageContaining("테이블이 공석입니다");
    }

    @DisplayName("손님수를 수정할경우 값이 변경")
    @Test
    public void returnNumberOfGuests() {
        int numberOfGuests = Arbitraries.integers().between(2, 100).sample();
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(2)
                .empty(false).build();

        orderTable.changeNumberOfGuests(numberOfGuests);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}

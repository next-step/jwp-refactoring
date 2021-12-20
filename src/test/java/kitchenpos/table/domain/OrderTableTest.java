package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import kitchenpos.exception.CannotUpdatedException;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("테이블 도메인 테스트")
class OrderTableTest {

    @Test
    @DisplayName("테이블 상태 변경")
    void updateEmpty() {
        OrderTable orderTable = OrderTable.of(1, false);
        assertFalse(orderTable.isEmpty());

        orderTable.updateEmpty(true);
        assertTrue(orderTable.isEmpty());
    }

    @DisplayName("단체 지정된 테이블의 상태는 변경 불가")
    @Test
    void validateUpdateEmpty() {
        OrderTable orderTable = OrderTable.of(1, false);
        orderTable.setTableGroup(TableGroup.create());

        assertThatThrownBy(() -> orderTable.updateEmpty(Boolean.TRUE))
            .isInstanceOf(CannotUpdatedException.class)
            .hasMessage("단체지정된 테이블은 변경할 수 없습니다.");
    }

    @DisplayName("주문 진행중인 테이블의 상태는 변경 불가")
    @Test
    void validateUpdateEmptyOnGoingOrder() {
        OrderTable orderTable = OrderTable.of(1, false);
        Order.of(orderTable, OrderStatus.COOKING, new ArrayList<>());

        assertThatThrownBy(() -> orderTable.updateEmpty(Boolean.TRUE))
            .isInstanceOf(CannotUpdatedException.class)
            .hasMessage("주문이 완료되지 않은 테이블이 있습니다.");
    }

    @Test
    @DisplayName("테이블의 손님 수 변경")
    void updateNumberOfGuests() {
        OrderTable orderTable = OrderTable.of(1, true);

        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(2))
            .isInstanceOf(CannotUpdatedException.class)
            .hasMessage("빈 테이블의 손님수는 변경 할 수 없습니다.");

        orderTable.updateEmpty(false);
        orderTable.updateNumberOfGuests(2);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }
}
package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import kitchenpos.common.exception.CannotUpdatedException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("테이블 도메인 테스트")
class OrderTableTest {

    @Test
    @DisplayName("테이블 상태 변경")
    void updateEmpty() {
        OrderTable orderTable = OrderTable.of(1, false);
        assertFalse(orderTable.isEmpty());

        orderTable.changeEmpty(true);
        assertTrue(orderTable.isEmpty());
    }

    @DisplayName("단체 지정된 테이블의 상태는 변경 불가")
    @Test
    void validateUpdateEmpty() {
        OrderTable orderTable = OrderTable.of(1, false);
        ReflectionTestUtils.setField(orderTable, "tableGroupId", 1L);

        assertThatThrownBy(() -> orderTable.changeEmpty(true))
            .isInstanceOf(CannotUpdatedException.class)
            .hasMessage("단체지정된 테이블은 변경할 수 없습니다.");
    }

    @DisplayName("주문 진행중인 테이블의 상태는 변경 불가")
    @Test
    void validateUpdateEmptyOnGoingOrder() {
        final OrderTable orderTable = OrderTable.of(1, false);
        orderTable.changeTableStatus(TableStatus.ORDERED);
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
            .isInstanceOf(CannotUpdatedException.class)
            .hasMessage("주문이 완료되지 않은 테이블이 있습니다.");
    }

    @Test
    @DisplayName("테이블의 손님 수 변경")
    void updateNumberOfGuests() {
        OrderTable orderTable = OrderTable.of(1, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
            .isInstanceOf(CannotUpdatedException.class)
            .hasMessage("빈 테이블의 손님수는 변경 할 수 없습니다.");

        orderTable.changeEmpty(false);
        orderTable.changeNumberOfGuests(2);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }
}
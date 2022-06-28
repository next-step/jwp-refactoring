package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {
    @DisplayName("초기화 테스트")
    @Test
    void from() {
        OrderTable orderTable = OrderTable.from(1L, 3, false);
        assertThat(orderTable).isEqualTo(orderTable);
    }

    @DisplayName("예약시 비어있지 않은 경우 테스트")
    @Test
    void reserveWithNotEmpty() {
        OrderTable orderTable = OrderTable.from(1L, 3, false);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.reserve(1L))
                .withMessage("주문테이블이 비어있어야 합니다.");
    }

    @DisplayName("예약시 단체지정 이미 있는 경우 테스트")
    @Test
    void reserveWithAlreadyTableGroup() {
        OrderTable orderTable = OrderTable.from(1L, 3, true);
        orderTable.reserve(2L);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.reserve(1L))
                .withMessage("단체지정이 없어야 합니다.");
    }

    @DisplayName("테이블 빈 여부 변경시 단체지정 이미 있는 경우 테스트")
    @Test
    void changeEmptyWithAlreadyTableGroup() {
        OrderTable orderTable = OrderTable.from(1L, 3, true);
        orderTable.reserve(2L);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeEmpty(true))
                .withMessage("단체지정이 없어야 합니다.");
    }

    @DisplayName("테이블 손님수 변경시 비어있는 경우 테스트")
    @Test
    void changeNumberOfGuestsWithEmpty() {
        OrderTable orderTable = OrderTable.from(1L, 3, true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeNumberOfGuests(5))
                .withMessage("주문테이블이 비어있으면 안됩니다.");
    }
}

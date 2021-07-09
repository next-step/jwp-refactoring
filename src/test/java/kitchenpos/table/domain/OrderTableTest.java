package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("주문 테이블의 빈 테이블 여부를 변경할시 단체 지정에 등록되지 않은 주문테이블 이어야 한다.")
    @Test
    void changeEmptyTest_wrongOrderTable() {
        // given
        OrderTable orderTable1 = new OrderTable(1, false);
        OrderTable orderTable2 = new OrderTable(1, false);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> orderTable1.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님수를 변경시 손님수는 0명 이상이어야 한다.")
    @Test
    void changeNumberOfGuestsTest_wrongNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(0, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님수를 변경시 빈 주문 테이블이 아니어야 한다.")
    @Test
    void changeNumberOfGuestsTest_emptyOrderTable() {
        // given
        OrderTable orderTable = new OrderTable(0, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

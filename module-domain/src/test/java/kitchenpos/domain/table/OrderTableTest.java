package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {
    @Test
    @DisplayName("테이블 정보를 변경할 수 있다.")
    void changeTableInfo() {
        OrderTable orderTable = OrderTable.of(4, true);
        OrderTable updateTable = OrderTable.of(5, false);

        orderTable.updateEmpty(updateTable);
        orderTable.updateNumberOfGuests(updateTable);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(updateTable.getNumberOfGuests());
        assertThat(orderTable.isEmpty()).isEqualTo(updateTable.isEmpty());
    }

    @Test
    @DisplayName("손님수가 0보다 작으면 예외를 반환한다.")
    void createFail() {
        assertThatThrownBy(() -> OrderTable.of(-1, true))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

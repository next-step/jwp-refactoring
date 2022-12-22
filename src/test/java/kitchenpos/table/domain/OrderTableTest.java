package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;

class OrderTableTest {
    @Test
    void 생성() {
        OrderTable orderTable = new OrderTable(1L, null, 1, false);

        assertAll(
                () -> assertThat(orderTable.getId()).isEqualTo(1L),
                () -> assertThat(orderTable.getTableGroup()).isNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(1),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }
}

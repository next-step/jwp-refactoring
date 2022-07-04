package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {
    @DisplayName("주문 테이블을 비어있게 변경한다")
    @Test
    void changeEmpty() {
        OrderTable orderTable = new OrderTable(1L, false);
        orderTable.changeEmpty(true);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 그룹에 설정되어 있다면 주분 테이블을 변경할 수 없다.")
    @Test
    void changeEmpty_exist_table_group() {
        assertThatThrownBy(() -> {
            OrderTable orderTable = new OrderTable(false, new TableGroup(1L));
            orderTable.changeEmpty(true);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경한다")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(5, false);
        orderTable.changeNumberOfGuests(3);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("0 보다 손님 수로 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_negative() {
        assertThatThrownBy(() -> {
            OrderTable orderTable = new OrderTable(5, false);
            orderTable.changeNumberOfGuests(-1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있다면 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_empty() {
        assertThatThrownBy(() -> {
            OrderTable orderTable = new OrderTable(5, true);
            orderTable.changeNumberOfGuests(3);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}

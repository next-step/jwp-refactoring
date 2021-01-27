package kitchenpos.table;

import kitchenpos.table.dto.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("상태 변경 테스트")
    @Test
    public void changeEmptyTest() {
        OrderTable orderTable = new OrderTable();
        orderTable.changeEmpty(true);
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("상태 변경 불가능한 경우 - 테이블이 단체 지정되있는 경우")
    @Test
    public void invalidCase() {
        OrderTable orderTable = new OrderTable(1l);
        assertThatThrownBy(() -> {
            orderTable.changeEmpty(true);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수 변경")
    @Test
    public void changeNumberOfGuest() {
        OrderTable orderTable = new OrderTable();
        orderTable.changeNumberOfGuests(1);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(1);
    }

    @DisplayName("방문한 손님 수 변경 불가능한 경우 - 수가 유효하지 않은 경우")
    @Test
    public void invalidCase1() {
        OrderTable orderTable = new OrderTable();
        assertThatThrownBy(() -> {
            orderTable.changeNumberOfGuests(-1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수 변경 불가능한 경우 - 테이블이 비어있는 경우")
    @Test
    public void invalidCase2() {
        OrderTable orderTable = new OrderTable();
        orderTable.changeEmpty(true);
        assertThatThrownBy(() -> {
            orderTable.changeNumberOfGuests(1);
        }).isInstanceOf(IllegalArgumentException.class);
    }


}

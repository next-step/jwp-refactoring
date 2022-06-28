package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("OrderTable의 정상 생성을 확인한다.")
    void createOrderTable() {
        OrderTable orderTable = new OrderTable(10, false);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("손님의 숫자가 유효하지 않을 경우 에러를 던진다.")
    void createOrderTableFail() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new OrderTable(-1, false));
    }

    @Test
    @DisplayName("빈 상태를 변경한다.")
    void changeEmpty() {
        OrderTable orderTable = new OrderTable(10, false);
        orderTable.changeEmpty(true);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("그룹에 속해있는 테이블일 경우 빈 상태를 변경할 수 없다.")
    void changeEmptyFail() {
        OrderTable orderTable = new OrderTable(10, false);
        orderTable.changeTableGroup(10L);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderTable.changeEmpty(true));
    }

    @Test
    @DisplayName("테이블 손님의 수를 변경한다.")
    void updateNumberOfGuests() {
        OrderTable orderTable = new OrderTable(10, false);
        orderTable.updateNumberOfGuests(20);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(20);
    }

    @Test
    @DisplayName("비어있는 테이블은 손님의 수를 변경할 수 없다.")
    void updateNumberOfGuestsFail() {
        OrderTable orderTable = new OrderTable(10, true);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderTable.updateNumberOfGuests(20));
    }

}

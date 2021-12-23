package kitchenpos.order.domain;

import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("테이블 관련 테스트")
class OrderTableTest {

    @Test
    @DisplayName("테이블을 등록한다.")
    void createTableTest() {
        OrderTable orderTable = new OrderTable(1L, 8, true);

        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isEqualTo(1L),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(8),
                () -> assertThat(orderTable.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("테이블 상태를 수정한다.")
    void modifyTableTest() {
        OrderTable orderTable = new OrderTable(1L, 8, true);
        orderTable.enterGuest();
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블 안원 수를 수정한다.")
    void modifyTableGuestCountTest() {
        OrderTable orderTable = new OrderTable(1L, 8, true);
        orderTable.enterGuest();
        orderTable.seatNumberOfGuests(5);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("테이블 안원 수를 0 미만으로 수정하면 에러 처리")
    void modifyTableGuestLessThanZeroCountTest() {
        assertThatThrownBy(() ->{
            new OrderTable(1L, -1, true);
        }).isInstanceOf(InputTableDataException.class)
                .hasMessageContaining(InputTableDataErrorCode.THE_NUMBER_OF_GUESTS_IS_NOT_LESS_THAN_ZERO.errorMessage());
    }

}

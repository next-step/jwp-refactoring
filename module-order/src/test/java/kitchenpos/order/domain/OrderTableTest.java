package kitchenpos.order.domain;

import kitchenpos.common.exception.InputDataErrorCode;
import kitchenpos.common.exception.InputDataException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("테이블 단위 테스트")
class OrderTableTest {

    @Autowired
    private OrderTableValidator orderTableValidator;

    @Test
    @DisplayName("테이블을 등록한다.")
    void createTableTest() {
        OrderTable orderTable = new OrderTable(5, false);
        assertAll(
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(5),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("테이블 상태를 수정한다.")
    void modifyTableTest() {
        OrderTable orderTable = new OrderTable(5, false);
        orderTable.enterGuest();
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블 인원 수를 수정한다.")
    void modifyTableGuestCountTest() {
        OrderTable orderTable = new OrderTable(5, false);
        orderTable.enterGuest();
        orderTable.seatNumberOfGuests(8);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(8);
    }

    @Test
    @DisplayName("테이블 인원 수를 0 미만으로 수정하면 에러 처리")
    void modifyTableGuestLessThanZeroCountTest() {
        OrderTable orderTable = new OrderTable(5, false);
        Assertions.assertThatThrownBy(() ->{
            orderTable.seatNumberOfGuests(-2);
        }).isInstanceOf(InputDataException.class)
                .hasMessageContaining(InputDataErrorCode.THE_NUMBER_OF_GUESTS_IS_LESS_THAN_ZERO.errorMessage());
    }

}

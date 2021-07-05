package kitchenpos.table.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(1L, 1L, 5, false);
    }

    @DisplayName("주문테이블 그룹핑 시에, 이미 그룹핑 된 주문테이블이면 예외처리한다.")
    @Test
    void checkIfAlreadyGroupedTest() {
        assertThatThrownBy(() -> {
            orderTable.checkIfAlreadyGrouped();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 숫자를 변경한다.")
    @Test
    void changeNumberOfGuestsTest_1() {
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);

        orderTable.changeNumberOfGuests(4);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("테이블의 손님 숫자 변경 수가 올바르지 않으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsTest_2() {
        assertThatThrownBy(() -> {
            orderTable.changeNumberOfGuests(-1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어있으면 손님 숫자를 변경할 수 없다..")
    @Test
    void changeNumberOfGuestsTest_3() {
        orderTable.finishTable();

        assertThatThrownBy(() -> {
            orderTable.changeNumberOfGuests(4);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}

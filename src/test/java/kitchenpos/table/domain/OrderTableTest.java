package kitchenpos.table.domain;

import static kitchenpos.fixture.TableFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = 테이블_생성(3, false);
    }

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        //given
        int numberOfGuests = 5;
        boolean empty = true;

        //when
        OrderTable newOrderTable = 테이블_생성(numberOfGuests, empty);

        //then
        assertThat(newOrderTable).isNotNull();
        assertThat(newOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
        assertThat(newOrderTable.isEmpty()).isEqualTo(empty);
    }

    @DisplayName("테이블의 빈 테이블 여부를 변경한다.")
    @Test
    void changeEmpty() {
        //given
        boolean empty = true;

        //when
        orderTable.changeEmpty(empty);

        //then
        assertThat(orderTable.isEmpty()).isEqualTo(empty);
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        int numberOfGuests = 10;

        //when
        orderTable.changeNumberOfGuests(numberOfGuests);

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("테이블의 손님 수를 0 미만으로 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_invalidMinusGuests() {
        //when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_invalidEmptyTable() {
        //given
        orderTable.changeEmpty(true);

        //when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(10))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

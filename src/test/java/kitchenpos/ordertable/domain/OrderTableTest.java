package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.ordertable.exception.IllegalNumberOfGuestsException;
import kitchenpos.ordertable.exception.TableChangeNumberOfGuestsException;
import kitchenpos.ordertable.exception.TableUpdateStateException;
import kitchenpos.ordertable.vo.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("주문 테이블 생성")
    @Test
    void constructor() {
        //given, when
        OrderTable orderTable = new OrderTable(new NumberOfGuests(6), false);
        OrderTable expectedTable = new OrderTable(new NumberOfGuests(6), false);

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedTable.getNumberOfGuests());
    }

    @DisplayName("테이블 상태 업데이트")
    @Test
    void updateEmpty() {
        //given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(6), false);

        //when
        orderTable.updateTableStatus(true);

        //then
        assertThat(orderTable.isOrderClose()).isTrue();
    }

    @DisplayName("그룹화된 테이블은 상태 업데이트 불가")
    @Test
    void updateEmpty_exception1() {
        //given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), true);
        orderTable.groupIn(1L);

        //when,then
        assertThatThrownBy(() -> orderTable.updateTableStatus(true))
            .isInstanceOf(TableUpdateStateException.class);
    }


    @DisplayName("테이블 방문 손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), false);
        NumberOfGuests changeNumber = new NumberOfGuests(4);

        //when
        orderTable.changeNumberOfGuests(changeNumber);

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(changeNumber);
    }

    @DisplayName("변경 요청 방문 손님 수는 0 이상이어야 한다.")
    @Test
    void changeNumberOfGuests_exception1() {
        //given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(6), false);
        //when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(new NumberOfGuests(-1)))
            .isInstanceOf(IllegalNumberOfGuestsException.class);
    }

    @DisplayName("주문종료 상태의 테이블은 방문손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_exception2() {
        //given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(6), true);
        NumberOfGuests changeNumber = new NumberOfGuests(2);

        //when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(changeNumber))
            .isInstanceOf(TableChangeNumberOfGuestsException.class);
    }
}

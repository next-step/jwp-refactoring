package domain.order;

import domain.order.exception.CannotChangeEmptyException;
import domain.order.exception.CannotChangeGuestEmptyTableException;
import domain.order.exception.CannotOrderEmptyTableException;
import domain.order.exception.CannotRegisterGroupException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("이미 그룹화된 테이블이라면 테이블의 주문 상태를 변경할 수 없다.")
    @Test
    void changeEmpty() {
        //given
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTable orderTable2 = OrderTable.of(2, true);
        OrderTableGroup orderTableGroup = OrderTableGroup.of(1L, Lists.list(orderTable1, orderTable2));
        orderTableGroup.grouped();

        //when
        assertThatThrownBy(() -> orderTable1.changeEmpty(false))
                .isInstanceOf(CannotChangeEmptyException.class); //then
    }

    @DisplayName("테이블 인원수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = OrderTable.of(4, false);

        //when
        orderTable.changeNumberOfGuests(3);

        //then
        assertThat(orderTable.getNumberOfGuests().getValue()).isEqualTo(3);
    }

    @DisplayName("빈 테이블의 인원수는 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsInEmptyTable() {
        //given
        OrderTable orderTable = OrderTable.of(4, true);

        //when
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
                .isInstanceOf(CannotChangeGuestEmptyTableException.class);
    }

    @DisplayName("테이블 그룹화된 아이디를 등록한다.")
    @Test
    void registerGroup() {
        //given
        OrderTable orderTable = OrderTable.of(4, true);

        //when
        orderTable.registerGroup(1L);

        //then
        assertThat(orderTable.getTableGroupId()).isEqualTo(1L);
    }

    @DisplayName("테이블 그룹을 지으려면 주문 불가능 상태여야한다.")
    @Test
    void registerGroupInvalid1() {
        //given
        OrderTable orderTable = OrderTable.of(4, false);

        //when
        assertThatThrownBy(() -> orderTable.registerGroup(1L))
                .isInstanceOf(CannotRegisterGroupException.class);
    }

    @DisplayName("테이블 그룹을 지으려면 아이디는 필수값이다.")
    @Test
    void registerGroupInvalid2() {
        //given
        OrderTable orderTable = OrderTable.of(4, true);

        //when
        assertThatThrownBy(() -> orderTable.registerGroup(null))
                .isInstanceOf(CannotRegisterGroupException.class);
    }

    @DisplayName("그룹화된 주문테이블은 그룹해제 후 그룹이 가능하다.")
    @Test
    void registerGroupInvalid3() {
        //given
        OrderTable orderTable = OrderTable.of(4, true);
        orderTable.registerGroup(1L);

        //when
        assertThatThrownBy(() -> orderTable.registerGroup(2L))
                .isInstanceOf(CannotRegisterGroupException.class);
    }

    @DisplayName("주문테이블이 주문 불가능 상태일 경우 주문할 수 없다.")
    @Test
    void createOrderExceptionIfTableEmptyIsTrue() {
        //given
        OrderTable orderTable = OrderTable.of(4, true);

        //when
        assertThatThrownBy(() -> orderTable.ordered(OrderLineItems.of(Lists.list())))
                .isInstanceOf(CannotOrderEmptyTableException.class); //then
    }
}

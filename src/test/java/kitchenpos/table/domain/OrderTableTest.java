package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("인원을 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = OrderTable.of(10, false);
        //when
        orderTable.changeNumberOfGuests(5);
        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("인원을 변경은 0명 이상만 가능하다.")
    @Test
    void changeNumberOfGuestsMinus() {
        //given
        OrderTable orderTable = OrderTable.of(10, false);
        //when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈테이블은 인원병경이 불가능하다.")
    @Test
    void changeNumberOfGuestsEmpty() {
        //given
        OrderTable orderTable = OrderTable.of(0, true);
        //when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(6))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        //given
        OrderTable orderTable = OrderTable.of(10, false);
        //when
        orderTable.changeEmpty(true);
        //then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("그룹 지정이 되어 있으면 상태를 변경할 수 없다.")
    @Test
    void changeEmptyGroup() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 0, true);
        OrderTable orderTable2 = new OrderTable(1L, null, 0, true);
        TableGroup tableGroup = new TableGroup(1L, Arrays.asList(orderTable2, orderTable1));
        OrderTable orderTable = OrderTable.of(0, true);
        orderTable.group(tableGroup);
        //when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);

    }


    @DisplayName("그룹 지정이 되어 잇으면 그룹아이디 값을 반환한다.")
    @Test
    void findTableGroupId() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 0, true);
        OrderTable orderTable2 = new OrderTable(1L, null, 0, true);
        TableGroup tableGroup = new TableGroup(1L, Arrays.asList(orderTable2, orderTable1));
        OrderTable orderTable = OrderTable.of(0, true);
        orderTable.group(tableGroup);
        //when
        Long tableGroupId = orderTable.findTableGroupId();
        //then
        assertThat(tableGroupId).isEqualTo(1L);

    }

    @DisplayName("그룹 지정이 되어 있지 않으면 null이 반환된다.")
    @Test
    void findTableGroupIdNull() {
        //given
        OrderTable orderTable = OrderTable.of(0, true);
        //when
        Long tableGroupId = orderTable.findTableGroupId();
        //then
        assertThat(tableGroupId).isNull();

    }
}

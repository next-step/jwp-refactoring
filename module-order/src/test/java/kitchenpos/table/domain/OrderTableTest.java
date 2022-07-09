package kitchenpos.table.domain;



import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



class OrderTableTest {

    @Test
    @DisplayName("테이블 그룹이 존재하면 변경할수 없다.")
    void changeEmptyValid() {
        //given
        OrderTable orderTable = new OrderTable(1L,3, false);

        //when & then
        assertThatIllegalStateException()
                .isThrownBy(orderTable::changeEmptyTable);
    }


    @Test
    @DisplayName("방문자변경은 0명이상 이어야 합니다.")
    void createNumberOfGuests() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderTable(-1, false));
    }

    @Test
    @DisplayName("방문자는 0명이상 이어야 합니다.")
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(1L,3, false);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeNumberOfGuests(-1));
    }

    @Test
    @DisplayName("빈테이블일 경우 방문자를 변경할 수 없습니다.")
    void changeNumberOfGuestsIsEmptyTable() {
        //given
        OrderTable orderTable = new OrderTable(3, false);

        //when
        orderTable.changeEmptyTable();

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeNumberOfGuests(2));
    }


}

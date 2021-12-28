package kitchenpos.table;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {


    @DisplayName("주문 테이블 생성")
    @Test
    void createTable() {

        //given
        final int numberOfGuests = 10;
        final boolean empty = true;

        //when
        OrderTable orderTable = OrderTable.setting(numberOfGuests, empty);

        //then
        assertThat(orderTable).isNotNull();
    }

    @DisplayName("주문 테이블 단체지정")
    @Test
    void groupingTable() {

        //given
        final int numberOfGuests = 10;
        final boolean empty = true;

        OrderTable orderTable = OrderTable.setting(numberOfGuests, empty);
        long tableGroupId = 1L;

        //when
        orderTable.grouping(tableGroupId);

        //then
        assertThat(orderTable.getTableGroupId()).isNotNull();
    }

    @DisplayName("주문 테이블 손님 수 조정하기")
    @Test
    void changeTableNumberOfGuests() {

        //given
        final int numberOfGuests = 10;
        final boolean empty = false;
        final int changedNumberOfGuests = 5;
        OrderTable orderTable = OrderTable.setting(numberOfGuests, empty);

        //when
        orderTable.changeNumberOfGuests(changedNumberOfGuests);

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(changedNumberOfGuests);
    }

    @DisplayName("주문 테이블 손님 수 음수로 조정하기")
    @Test
    void changeTableNumberOfGuestsToNegative() {

        //given
        final int numberOfGuests = 10;
        final boolean empty = false;
        final int changedNumberOfGuests = -1;
        OrderTable orderTable = OrderTable.setting(numberOfGuests, empty);

        //when
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(changedNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

package kitchenpos.table.domain;

import kitchenpos.table.exception.OrderTableException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class OrderTableTest {

    OrderTable 테이블;

    @Test
    @DisplayName("테이블 비어있는지 여부를 변경하면 변경된다.")
    void changeEmpty() {
        //given
        테이블 = new OrderTable(1L, 2, false);

        //when
        테이블.changeEmpty(true);

        //then
        assertThat(테이블.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 비어있는지 여부를 변경할 때, 단체가 지정되어 있으면 변경할 수 없다.")
    void changeEmptyHasTableGroup() {
        //given
        TableGroup 단체 = new TableGroup(1L, LocalDateTime.now());
        테이블 = new OrderTable(1L, 단체.getId(), 2, false);

        //then
        assertThatThrownBy(() -> 테이블.changeEmpty(true))
                .isInstanceOf(OrderTableException.class)
                .hasMessageContaining(OrderTableException.ORDER_TALBE_ALREADY_HAS_GROUP_MSG);
    }


    @Test
    @DisplayName("테이블의 손님 수를 변경하면 변경된다.")
    void changeNumberOfGuests() {
        //given
        테이블 = new OrderTable(1L, 2, false);

        //when
        테이블.changeNumberOfGuests(3);

        //then
        assertThat(테이블.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    @DisplayName("유효하지 않은 테이블의 손님 수로 변경하면 변경되지 않는다.")
    void changeNumberOfGuestsInvalidNumberOfGuests() {
        //given
        테이블 = new OrderTable(1L, 2, false);

        //then
        assertThatThrownBy(() -> 테이블.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있는 테이블의 손님 수를 변경하면 변경되지 않는다.")
    void changeNumberOfGuestsIsEmpty() {
        //given
        테이블 = new OrderTable(1L, 2, true);

        //then
        assertThatThrownBy(() -> 테이블.changeNumberOfGuests(3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 단체를 해제하면 해제된다.")
    void unGroup() {
        //given
        TableGroup 단체 = new TableGroup(1L, LocalDateTime.now());
        테이블 = new OrderTable(1L, 단체.getId(), 2, false);

        //when
        테이블.unGroup();

        //then
        assertThat(테이블.getTableGroupId()).isNull();
    }
}
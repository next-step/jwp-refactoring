package kitchenpos.table.domain;

import kitchenpos.table.domain.exception.CannotOrderEmptyTableException;
import kitchenpos.table.domain.exception.UngroupTableException;
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
        OrderTableGroup.createWithMapping(OrderTables.of(Lists.list(orderTable1, orderTable2)));

        //when
        assertThatThrownBy(() -> orderTable1.changeEmpty(false))
                .isInstanceOf(UngroupTableException.class); //then
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
                .isInstanceOf(CannotOrderEmptyTableException.class);
    }

    @DisplayName("테이블 그룹을 지으려면 주문 불가능 상태여야한다.")
    @Test
    void validateTableGroupable() {
        //given
        OrderTable orderTable = OrderTable.of(4, false);

        //when
        assertThatThrownBy(orderTable::validateTableGroupable)
                .isInstanceOf(UngroupTableException.class);
    }
}

package kitchenpos.table.domain;

import kitchenpos.table.domain.exception.CannotRegisterGroupException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableGroupTest {

    @Test
    void create() {
        //given
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTable orderTable2 = OrderTable.of(2, true);
        OrderTable orderTable3 = OrderTable.of(3, true);

        //when
        OrderTableGroup actual = OrderTableGroup.of(Lists.list(orderTable1, orderTable2, orderTable3));

        //then
        assertThat(actual.getOrderTables()).contains(orderTable1, orderTable2, orderTable3);
    }

    @DisplayName("테이블 그룹을 지으려면 주문 불가능 상태만 가능하다.")
    @Test
    void createNotEmptyTableException() {
        //given
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTable orderTable2 = OrderTable.of(2, false);
        OrderTable orderTable3 = OrderTable.of(3, true);
        OrderTableGroup orderTableGroup = OrderTableGroup.of(Lists.list(orderTable1, orderTable2, orderTable3));

        //when
        assertThatThrownBy(orderTableGroup::grouped)
                .isInstanceOf(CannotRegisterGroupException.class); //then
    }
}

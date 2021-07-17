package domain.order;

import domain.order.exception.CannotRegisterGroupException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableGroupTest {

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private OrderTable orderTable3;

    @BeforeEach
    void setUp() {
        orderTable1 = OrderTable.of(4, true);
        orderTable2 = OrderTable.of(2, true);
        orderTable3 = OrderTable.of(3, true);
    }

    @Test
    void create() {
        //when
        OrderTableGroup actual = OrderTableGroup.of(Lists.list(orderTable1, orderTable2, orderTable3));

        //then
        assertThat(actual.getOrderTables()).contains(orderTable1, orderTable2, orderTable3);
    }

    @DisplayName("테이블 그룹을 지으려면 주문 불가능 상태만 가능하다.")
    @Test
    void createNotEmptyTableException() {
        //given
        OrderTable orderTable = OrderTable.of(4, false);
        OrderTableGroup orderTableGroup = OrderTableGroup.of(Lists.list(orderTable1, orderTable2, orderTable3, orderTable));

        //when
        assertThatThrownBy(orderTableGroup::grouped)
                .isInstanceOf(CannotRegisterGroupException.class); //then
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungrouped() {
        //given
        OrderTableGroup orderTableGroup = OrderTableGroup.of(99L, Lists.list(orderTable1, orderTable2, orderTable3));
        orderTableGroup.grouped();

        //when
        orderTableGroup.ungrouped();

        //then
        assertThat(orderTableGroup.getOrderTables().get(0).getTableGroupId()).isNull();
        assertThat(orderTableGroup.getOrderTables().get(1).getTableGroupId()).isNull();
        assertThat(orderTableGroup.getOrderTables().get(2).getTableGroupId()).isNull();
    }
}

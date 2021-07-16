package domain.order;

import domain.order.exception.CannotRegisterGroupException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderTablesTest {

    @DisplayName("테이블 그룹을 짓는다.")
    @Test
    void groupBy() {
        //given
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTable orderTable2 = OrderTable.of(2, true);
        OrderTable orderTable3 = OrderTable.of(3, true);
        OrderTables orderTables = OrderTables.of(Lists.list(orderTable1, orderTable2, orderTable3));

        //when
        orderTables.groupBy(1L);

        //then
        assertThat(orderTable1.getTableGroupId()).isEqualTo(1L);
        assertThat(orderTable2.getTableGroupId()).isEqualTo(1L);
        assertThat(orderTable3.getTableGroupId()).isEqualTo(1L);
    }

    @DisplayName("테이블 그룹을 지으려면 모든 테이블이 주문 불가능 상태여야한다.")
    @Test
    void groupByInvalid1() {
        //given
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTable orderTable2 = OrderTable.of(2, false);
        OrderTable orderTable3 = OrderTable.of(3, true);
        OrderTables orderTables = OrderTables.of(Lists.list(orderTable1, orderTable2, orderTable3));

        //when
        assertThatThrownBy(() -> orderTables.groupBy(1L))
                .isInstanceOf(CannotRegisterGroupException.class); //then
    }

    @DisplayName("테이블 그룹을 지으려면 모든 테이블이 그룹화되어있지 않아야한다.")
    @Test
    void groupByInvalid2() {
        //given
        OrderTable orderTable1 = OrderTable.of(4, true);
        orderTable1.registerGroup(2L);
        OrderTable orderTable2 = OrderTable.of(2, true);
        OrderTable orderTable3 = OrderTable.of(3, true);
        OrderTables orderTables = OrderTables.of(Lists.list(orderTable1, orderTable2, orderTable3));

        //when
        assertThatThrownBy(() -> orderTables.groupBy(1L))
                .isInstanceOf(CannotRegisterGroupException.class); //then
    }
}

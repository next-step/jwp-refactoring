package domain.order;

import domain.order.exception.CannotRegisterGroupException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderTablesTest {

    private OrderTable orderTable1 = OrderTable.of(4, true);
    private OrderTable orderTable2 = OrderTable.of(2, true);
    private OrderTable orderTable3 = OrderTable.of(3, true);

    @BeforeEach
    void setUp() {
         orderTable1 = OrderTable.of(4, true);
         orderTable2 = OrderTable.of(2, true);
         orderTable3 = OrderTable.of(3, true);
    }

    @DisplayName("테이블 그룹을 짓는다.")
    @Test
    void groupBy() {
        //given
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
        OrderTable orderTable = OrderTable.of(5, false);
        OrderTables orderTables = OrderTables.of(Lists.list(orderTable1, orderTable2, orderTable3, orderTable));

        //when
        assertThatThrownBy(() -> orderTables.groupBy(1L))
                .isInstanceOf(CannotRegisterGroupException.class); //then
    }

    @DisplayName("테이블 그룹을 지으려면 모든 테이블이 그룹화되어있지 않아야한다.")
    @Test
    void groupByInvalid2() {
        //given
        orderTable2.registerGroup(2L);
        OrderTables orderTables = OrderTables.of(Lists.list(orderTable1, orderTable2, orderTable3));

        //when
        assertThatThrownBy(() -> orderTables.groupBy(1L))
                .isInstanceOf(CannotRegisterGroupException.class); //then
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungrouped() {
        //given
        OrderTables orderTables = OrderTables.of(Lists.list(orderTable1, orderTable2, orderTable3));
        orderTables.groupBy(99L);

        //when
        orderTables.ungroup();

        //then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
        assertThat(orderTable3.getTableGroupId()).isNull();
    }
}

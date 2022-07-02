package kitchenpos.domain;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.tablegroup.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {
    @Test
    @DisplayName("단체 지정을 할 수 있다.")
    void group() {
        //given
        OrderTables orderTables = new OrderTables(
                2,
                Arrays.asList(
                        OrderTable.of(5, true),
                        OrderTable.of( 10, true)
                )
        );

        //when
        TableGroup tableGroup = new TableGroup(orderTables);

        //then
        assertThat(tableGroup.getOrderTables().stream().map(OrderTable::getNumberOfGuests)).containsExactlyInAnyOrder(
                10, 5);
    }

    @Test
    @DisplayName("단체 지정을 해제 할 수 있다.")
    void ungroup() {
        //given
        OrderTable orderTable1 = OrderTable.of( 5, true);
        OrderTable orderTable2 = OrderTable.of(1, true);
        TableGroup tableGroup = new TableGroup(new OrderTables(2, Arrays.asList(orderTable1, orderTable2)));

        //when
        tableGroup.unGroup();

        //then
        assertThat(tableGroup.getOrderTables().stream().noneMatch(OrderTable::isGrouped)).isTrue();
    }

    @Test
    @DisplayName("단체 지정 내 속한 주문 테이블 개수가 2개 미만이면 단체 지정에 실패한다.")
    void create_failed_1() {
        //given
        OrderTables orderTables = new OrderTables(
                1,
                Arrays.asList(OrderTable.of( 10, true))
        );
        //then
        assertThatThrownBy(() -> new TableGroup(orderTables)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 내 속한 주문 테이블 중 이미 단체 지정이 되어있거나, 빈 테이블이 아닌 주문 테이블이 있으면 단체 지정에 실패한다.")
    void create_failed_2() {
        //given
        OrderTable orderTable1 = OrderTable.of(5, true);
        OrderTable orderTable2 = OrderTable.of(1, true);
        OrderTables orderTables = new OrderTables(
                2,
                Arrays.asList(
                        OrderTable.of( 5, false),
                        OrderTable.of(10, true)
                )
        );
        //then
        assertThatThrownBy(() -> new TableGroup(orderTables)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

}

package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.testfixtures.MenuTestFixtures;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.exception.GroupTablesException;
import kitchenpos.ordertable.exception.UngroupTablesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("테이블 그룹 생성")
    @Test
    void groupTables() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 6, true),
            new OrderTable(2L, 3, true));

        //when
        TableGroup tableGroup = new TableGroup();
        tableGroup.groupTables(orderTables);

        //then
        assertThat(tableGroup.getOrderTableList()).containsAll(orderTables);
    }

    @DisplayName("그룹 대상 테이블은 2개 이상이어야 한다.")
    @Test
    void groupTables_exception1() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 6, true));

        //when, then
        TableGroup tableGroup = new TableGroup();
        assertThatThrownBy(() -> tableGroup.groupTables(orderTables))
            .isInstanceOf(GroupTablesException.class);
    }

    @DisplayName("주문 가능한 테이블은 그룹이 될 수 없다.")
    @Test
    void groupTables_exception2() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 6, false),
            new OrderTable(2L, 3, true));

        //when, then
        TableGroup tableGroup = new TableGroup(1L);
        assertThatThrownBy(() -> tableGroup.groupTables(orderTables))
            .isInstanceOf(GroupTablesException.class);
    }

    @DisplayName("그룹 대상 테이블 리스트에 중복이 존재해서는 안된다.")
    @Test
    void groupTables_exception3() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 6, true),
            new OrderTable(1L, 6, true));

        //when, then
        TableGroup tableGroup = new TableGroup(1L);
        assertThatThrownBy(() -> tableGroup.groupTables(orderTables))
            .isInstanceOf(GroupTablesException.class);
    }

    @DisplayName("이미 그룹에 소속된 테이블은 그룹화 할 수 없다.")
    @Test
    void groupTables_exception4() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 6, true),
            new OrderTable(2L, 3, true));

        TableGroup tableGroup = new TableGroup(1L);
        tableGroup.groupTables(orderTables);

        //when,then
        TableGroup tableGroup2 = new TableGroup(2L);
        assertThatThrownBy(() -> tableGroup2.groupTables(orderTables))
            .isInstanceOf(GroupTablesException.class);
    }

    @DisplayName("그룹 해제")
    @Test
    void ungroup() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 6, true),
            new OrderTable(2L, 3, true));
        TableGroup tableGroup = new TableGroup(1L);
        tableGroup.groupTables(orderTables);

        //when
        tableGroup.ungroup();

        //then
        assertThat(tableGroup.getOrderTableSize()).isEqualTo(0);
        tableGroup.getOrderTableList()
            .stream()
            .forEach(orderTable -> assertThat(orderTable.getTableGroup()).isNull());
    }

    @DisplayName("계산완료 되지 않은 주문이 있는 경우 그룹 해제할 수 없다.")
    @Test
    void ungroup_exception() {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 6, true),
            new OrderTable(2L, 3, true));
        TableGroup tableGroup = new TableGroup(1L);
        tableGroup.groupTables(orderTables);

        Order order = new Order(orderTables.get(0), Arrays.asList(
            new OrderLineItem(MenuTestFixtures.서비스군만두, 2)));

        //when,then
        assertThatThrownBy(() -> tableGroup.ungroup())
            .isInstanceOf(UngroupTablesException.class);
    }
}

package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.message.TableGroupMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupTest {

    @Test
    @DisplayName("테이블 그룹 생성에 성공한다")
    void createTableGroupTest() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true),
                OrderTable.of(3, true)
        );

        // when
        TableGroup tableGroup = new TableGroup(orderTables);

        // then
        assertThat(tableGroup).isEqualTo(new TableGroup(orderTables));
    }

    @Test
    @DisplayName("테이블 그룹 생성시 주문 테이블의 개수가 2개 미만인 경우 생성에 실패한다")
    void createTableGroupThrownByLessThanTwoTablesTest() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                OrderTable.of(1, true)
        );

        // when & then
        assertThatThrownBy(() -> new TableGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroupMessage.CREATE_ERROR_MORE_THAN_TWO_ORDER_TABLES.message());
    }

    @Test
    @DisplayName("테이블 그룹 생성시 다른 테이블 그룹에 등록 된 주문 테이블이 주어진 경우 생성에 실패한다")
    void createTableGroupThrownByEnrolledOtherGroupTest() {
        // given
        OrderTable orderTable = OrderTable.of(1, true);
        List<OrderTable> otherOrderTables = Arrays.asList(
                orderTable,
                OrderTable.of(2, true)
        );
        TableGroup otherTableGroup = new TableGroup(otherOrderTables);
        otherTableGroup.group();

        List<OrderTable> orderTables = Arrays.asList(
                orderTable,
                OrderTable.of(2, true)
        );

        // when & then
        assertThatThrownBy(() -> new TableGroup(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroupMessage.CREATE_ERROR_OTHER_TABLE_GROUP_MUST_BE_NOT_ENROLLED.message());
    }

    @Test
    @DisplayName("테이블 그룹 지정에 성공한다")
    void groupOrderTablesTest() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true),
                OrderTable.of(3, true)
        );
        TableGroup tableGroup = new TableGroup(orderTables);

        // when
        tableGroup.group();

        // then
        boolean groupByGivenTableGroup = tableGroup.getOrderTables().getAll()
                .stream()
                .allMatch(table -> table.isGroupBy(tableGroup));
        assertThat(groupByGivenTableGroup).isTrue();
    }

    @Test
    @DisplayName("테이블 그룹 해지에 성공한다")
    void unGroupOrderTablesTest() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true),
                OrderTable.of(3, true)
        );
        TableGroup tableGroup = new TableGroup(orderTables);
        tableGroup.group();

        // when
        tableGroup.unGroup();

        // then
        boolean unGroupByGivenTableGroup = tableGroup.getOrderTables().getAll()
                .stream()
                .noneMatch(OrderTable::isEnrolledGroup);
        assertThat(unGroupByGivenTableGroup).isTrue();
    }
}

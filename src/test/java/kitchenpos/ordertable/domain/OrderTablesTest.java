package kitchenpos.ordertable.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {
    @DisplayName("단체 지정할 수 있는지 확인할 때, 빈 상태가 아니라면 예외가 발생한다.")
    @Test
    void groupEmptyException() {
        // given
        OrderTable orderTable1 = new OrderTable(new NumberOfGuests(4), false);
        OrderTable orderTable2 = new OrderTable(new NumberOfGuests(4), true);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> orderTables.validateGroup())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.NOT_EMPTY_STATUS_IN_ORDER_TABLES.getMessage());
    }

    @DisplayName("단체 지정할 수 있는지 확인할 때, 이미 단체지정 되어 있다면 예외가 발생한다.")
    @Test
    void hasGroupException() {
        // given
        OrderTable orderTable1 = new OrderTable(new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(new NumberOfGuests(4), true);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        orderTable1.setTableGroupId(tableGroup.getId());

        // when & then
        assertThatThrownBy(() -> orderTables.validateGroup())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLES_HAS_GROUP_TABLE.getMessage());
    }

    @DisplayName("주문 테이블 목록에 대해 단체 지정해제를 할 수 있다.")
    @Test
    void orderTablesUngroup() {
        // given
        OrderTable orderTable1 = new OrderTable(new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(new NumberOfGuests(4), true);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        orderTable1.setTableGroupId(tableGroup.getId());

        // when
        orderTables.ungroup();

        // then
        assertThat(orderTable1.getTableGroupId()).isNull();
    }
}

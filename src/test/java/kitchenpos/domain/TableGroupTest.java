package kitchenpos.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupTest {
    @DisplayName("단체 지정된 테이블을 해제할 수 있다.")
    @Test
    void unGroup() {
        // given
        OrderTable orderTable1 = new OrderTable(new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(new NumberOfGuests(4), true);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        orderTable1.setTableGroup(tableGroup);
        orderTable2.setTableGroup(tableGroup);

        Order order1 = new Order(orderTable1, OrderStatus.COMPLETION, LocalDateTime.now());
        Order order2 = new Order(orderTable2, OrderStatus.COMPLETION, LocalDateTime.now());

        // when
        tableGroup.ungroup(Arrays.asList(order1, order2));

        // then
        assertAll(
                () -> assertThat(orderTable1.getTableGroup()).isNull(),
                () -> assertThat(orderTable2.getTableGroup()).isNull()
        );
    }

    @DisplayName("단체 지정된 테이블을 해제할 때, 주문상태가 결제완료가 아니라면 예외가 발생한다.")
    @Test
    void unGroupException() {
        // given
        OrderTable orderTable1 = new OrderTable(new NumberOfGuests(4), true);
        OrderTable orderTable2 = new OrderTable(new NumberOfGuests(4), true);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        orderTable1.setTableGroup(tableGroup);
        orderTable2.setTableGroup(tableGroup);

        Order order1 = new Order(orderTable1, OrderStatus.MEAL, LocalDateTime.now());
        Order order2 = new Order(orderTable2, OrderStatus.COMPLETION, LocalDateTime.now());

        // when & then
        assertThatThrownBy(() -> tableGroup.ungroup(Arrays.asList(order1, order2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_STATUS_NOT_COMPLETE.getMessage());
    }
}

package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {
    @Test
    void 단체_지정된_테이블을_해제할_수_있다() {
        // given
        OrderTable firstOrderTable = new OrderTable(new NumberOfGuests(4), true);
        OrderTable secondOrderTable = new OrderTable(new NumberOfGuests(4), true);
        OrderTables orderTables = new OrderTables(Arrays.asList(firstOrderTable, secondOrderTable));

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        firstOrderTable.setTableGroup(tableGroup);
        secondOrderTable.setTableGroup(tableGroup);

        Order order1 = new Order(firstOrderTable, OrderStatus.COMPLETION, LocalDateTime.now());
        Order order2 = new Order(secondOrderTable, OrderStatus.COMPLETION, LocalDateTime.now());

        // when
        tableGroup.ungroup(Arrays.asList(order1, order2));

        // then
        assertAll(
                () -> assertThat(firstOrderTable.getTableGroup()).isNull(),
                () -> assertThat(secondOrderTable.getTableGroup()).isNull()
        );
    }

    @Test
    void 단체_지정된_테이블을_해제할_때_주문상태가_결제완료가_아니면_예외를_발생한다() {
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
                .hasMessageContaining(ErrorEnum.NOT_PAYMENT_ORDER.message());
    }
}

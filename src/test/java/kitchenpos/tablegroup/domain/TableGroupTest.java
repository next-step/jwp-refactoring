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
import org.junit.jupiter.api.Test;

public class TableGroupTest {
    @Test
    void 단체_지정된_테이블을_해제할_수_있다() {
        // given
        Order order1 = createCompleteOrder();
        Order order2 = createCompleteOrder();

        OrderTable firstOrderTable = order1.getOrderTable();
        OrderTable secondOrderTable = order2.getOrderTable();

        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                new OrderTables(Arrays.asList(firstOrderTable, secondOrderTable))
        );

        firstOrderTable.setTableGroup(tableGroup);
        secondOrderTable.setTableGroup(tableGroup);
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
        Order firstOrder = createCompleteOrder();
        Order secondOrder = createCompleteOrder();
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                new OrderTables(Arrays.asList(firstOrder.getOrderTable(), secondOrder.getOrderTable()))
        );

        firstOrder.setOrderStatus(OrderStatus.MEAL);

        // when & then
        assertThatThrownBy(() -> tableGroup.ungroup(Arrays.asList(firstOrder, secondOrder)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.NOT_PAYMENT_ORDER.message());
    }

    public static Order createCompleteOrder() {
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = new Order(orderTable, OrderStatus.COMPLETION, LocalDateTime.now());
        orderTable.updateEmpty(true);
        return order;
    }
}


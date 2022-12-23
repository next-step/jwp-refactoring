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
import org.junit.jupiter.api.Test;

public class TableGroupTest {
    @Test
    void 단체_지정된_테이블을_해제할_수_있다() {
        // given
        Order order1 = createOrder(OrderStatus.COMPLETION);
        Order order2 = createOrder(OrderStatus.COMPLETION);
        OrderTable firstOrderTable = new OrderTable(new NumberOfGuests(4), false);
        OrderTable secondOrderTable = new OrderTable(new NumberOfGuests(4), false);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        // when
        tableGroup.ungroup(Arrays.asList(order1, order2));

        // then
        assertAll(
                () -> assertThat(firstOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(secondOrderTable.getTableGroupId()).isNull()
        );
    }

    @Test
    void 단체_지정된_테이블을_해제할_때_주문상태가_결제완료가_아니면_예외를_발생한다() {
        // given
        Order firstOrder = createOrder(OrderStatus.MEAL);
        Order secondOrder = createOrder(OrderStatus.MEAL);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        firstOrder.setOrderStatus(OrderStatus.MEAL);

        // when & then
        assertThatThrownBy(() -> tableGroup.ungroup(Arrays.asList(firstOrder, secondOrder)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.NOT_PAYMENT_ORDER.message());
    }

    public static Order createOrder(OrderStatus orderStatus) {
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = Order.of(orderTable.getId(), null);
        order.setOrderStatus(orderStatus);
        orderTable.updateEmpty(true);
        return order;
    }
}


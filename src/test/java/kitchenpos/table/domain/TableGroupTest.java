package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("단체지정할 수 있다.")
    @Test
    void create() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 0, true);
        OrderTable orderTable2 = new OrderTable(1L, null, 0, true);

        //when
        TableGroup tableGroup = TableGroup.from(Arrays.asList(orderTable1, orderTable2));

        //then
        assertThat(tableGroup.getOrderTables().getUnmodifiableOrderTables().size()).isEqualTo(2);
    }

    @DisplayName("단체지정은 두개부터 가능하다.")
    @Test
    void createOne() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 0, true);

        //when & then
        assertThatThrownBy(() -> TableGroup.from(Collections.singletonList(orderTable1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정을 해제할 수 있다.")
    @Test
    void ungroup() {
        //given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, null, null, 1L);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, null, null, 1L);
        OrderTable orderTable1 = new OrderTable(1L, null, 0, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 0, true);

        TableGroup tableGroup = TableGroup.from(Arrays.asList(orderTable1, orderTable2));

        Order order1 = Order.of(1L, Collections.singletonList(orderLineItem1));
        order1.changeStatus(OrderStatus.COMPLETION);
        Order order2 = Order.of(2L, Collections.singletonList(orderLineItem2));
        order2.changeStatus(OrderStatus.COMPLETION);
        List<Order> orders = Arrays.asList(order1, order2);

        //when
        tableGroup.ungroup();
        //then
        assertThat(tableGroup.getOrderTables().getUnmodifiableOrderTables()).isEmpty();
    }


}

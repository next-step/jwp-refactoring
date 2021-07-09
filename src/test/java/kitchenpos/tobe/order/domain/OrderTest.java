package kitchenpos.tobe.order.domain;

import kitchenpos.tobe.menu.domain.Menu;
import kitchenpos.tobe.table.domain.OrderTable;
import kitchenpos.tobe.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @DisplayName("주문에 주문항목을 추가한다.")
    @Test
    void add() {
        Order order = Order.builder()
                .id(1L)
                .orderTable(new OrderTable(1L, new TableGroup(1L, new ArrayList<>(), LocalDateTime.now()), 4, false))
                .orderLineItems(new OrderLineItems())
                .orderedTime(LocalDateTime.now())
                .orderStatus(OrderStatus.MEAL)
                .build();

        OrderLineItem orderLineItem = OrderLineItem.builder()
                .id(1L)
                .order(new Order())
                .menu(Menu.builder().builder())
                .quantity(5L)
                .build();

        order.addOrderLineItem(orderLineItem);

        assertThat(order.getOrderLineItems()).isNotEmpty();
    }
}

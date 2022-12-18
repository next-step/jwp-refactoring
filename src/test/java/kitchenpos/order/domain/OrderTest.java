package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static kitchenpos.order.application.OrderService.ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE;
import static kitchenpos.order.domain.Orders.COMPLETION_CHANGE_EXCEPTION_MESSAGE;
import static kitchenpos.order.domain.Orders.ORDER_TABLE_NULL_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.*;

@DisplayName("주문")
class OrderTest {

    @DisplayName("주문 항목이 비어있을 수 없다.")
    @Test
    void constructor_fail_orderItem() {
        assertThatThrownBy(() -> new Orders(new OrderTable(), new OrderLineItems()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문 테이블은 비어있을 수 없다.")
    @Test
    void constructor_fail_orderTable() {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addAll(Collections.singletonList(new OrderLineItem(null, 1L, 1)));
        assertThatThrownBy(() -> new Orders(null, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void name() {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addAll(Collections.singletonList(new OrderLineItem(null, 1L, 1)));
        assertThatNoException().isThrownBy(() -> new Orders(new OrderTable(1L, new TableGroup(), 1, false), orderLineItems));
    }

    @DisplayName("주문상태를 식사중으로 변경한다.")
    @Test
    void changeMeal_success() {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addAll(Collections.singletonList(new OrderLineItem(null, 1L, 1)));
        Orders order = new Orders(new OrderTable(1L, new TableGroup(), 1, false), orderLineItems);
        order.meal();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문완료일 경우 주문상태를 변경할 수 없다.")
    @Test
    void changeMeal_fail_completion() {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addAll(Collections.singletonList(new OrderLineItem(null, 1L, 1)));
        Orders order = new Orders(new OrderTable(1L, new TableGroup(), 1, false), orderLineItems);
        order.complete();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
        assertThatThrownBy(order::meal)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(COMPLETION_CHANGE_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문상태를 완료로 변경한다.")
    @Test
    void nameCompletion() {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addAll(Collections.singletonList(new OrderLineItem(null, 1L, 1)));
        Orders order = new Orders(new OrderTable(1L, new TableGroup(), 1, false), orderLineItems);
        order.complete();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}

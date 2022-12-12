package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.order.application.OrderCrudService.ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE;
import static kitchenpos.order.domain.Order.COMPLETION_CHANGE_EXCEPTION_MESSAGE;
import static kitchenpos.order.domain.Order.ORDER_TABLE_NULL_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.*;

@DisplayName("주문")
class OrderTest {

    @DisplayName("주문 항목이 비어있을 수 없다.")
    @Test
    void constructor_fail_orderItem() {
        assertThatThrownBy(() -> new Order(1L, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문 테이블은 비어있을 수 없다.")
    @Test
    void constructor_fail_orderTable() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(1L, 1L, 1));

        assertThatThrownBy(() -> new Order(null, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void name() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(1L, 1L, 1));
        assertThatNoException().isThrownBy(() -> new Order(new OrderTable(1L, 1L, 1, true).getId(), orderLineItems));
    }

    @DisplayName("주문상태를 식사중으로 변경한다.")
    @Test
    void changeMeal_success() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(1L, 1L, 1));
        Order order = new Order(new OrderTable(1L, 1L, 1, true).getId(), orderLineItems);
        order.meal();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문완료일 경우 주문상태를 변경할 수 없다.")
    @Test
    void changeMeal_fail_completion() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(1L, 1L, 1));
        Order order = new Order(new OrderTable(1L, 1L, 1, true).getId(), orderLineItems);
        order.complete();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        assertThatThrownBy(order::meal)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(COMPLETION_CHANGE_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문상태를 완료로 변경한다.")
    @Test
    void nameCompletion() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(1L, 1L, 1));
        Order order = new Order(new OrderTable(1L, 1L, 1, true).getId(), orderLineItems);
        order.complete();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}

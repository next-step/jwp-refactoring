package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("주문")
class OrderTest {

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // given
        OrderTable orderTable = new OrderTable(5, false);
        OrderLineItem orderLineItem1 = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();


        // when
        Order order = Order.of(orderTable, Arrays.asList(orderLineItem1, orderLineItem2));

        // then
        assertThat(order).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("하나 이상의 주문 항목을 가져야 한다.")
    @Test
    void requiredOrderLineItem() {
        // given
        OrderTable orderTable = new OrderTable(5, false);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> Order.of(orderTable, new ArrayList<>()));
    }

    @DisplayName("주문 테이블이 등록 불가 상태인 경우 생성할 수 없다.")
    @Test
    void emptyOrderTable() {
        // given
        OrderTable orderTable = new OrderTable(5, true);
        OrderLineItem orderLineItem1 = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();


        // when / then
        assertThrows(IllegalArgumentException.class,
                () -> Order.of(orderTable, Arrays.asList(orderLineItem1, orderLineItem2)));
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeStatus() {
        // given
        OrderTable orderTable = new OrderTable(5, false);
        OrderLineItem orderLineItem1 = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();
        Order order = Order.of(orderTable, Arrays.asList(orderLineItem1, orderLineItem2));

        // when
        order.changeStatus(OrderStatus.COMPLETION);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("주문의 상태가 이미 완료된 경우 변경할 수 없다.")
    @Test
    void cantChangeStatus() {
        // given
        OrderTable orderTable = new OrderTable(5, false);
        OrderLineItem orderLineItem1 = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();
        Order order = Order.of(orderTable, Arrays.asList(orderLineItem1, orderLineItem2));
        order.changeStatus(OrderStatus.COMPLETION);

        // when / then
        assertThrows(IllegalArgumentException.class,
                () -> order.changeStatus(OrderStatus.MEAL));

    }
}

package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문에 주문 항목 추가")
    void addOrderLineTest() {

        //when
        Order order = new Order(1L, new OrderLineItem(1L, 10));

        //then
        assertThat(order.getOrderLineItems()).hasSize(1);
    }

    @Test
    @DisplayName("주문에 빈주문 항목 추가")
    void addEmptyOrderLineTest() {
        //given
        OrderTable orderTable = new OrderTable(10, false);

        //when & then
        assertThatThrownBy(
                () -> new Order(orderTable.getId(), Collections.emptyList())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("완료 상태의 주문의 상태 수정")
    void changeOrderStatusTest() {
        //given
        OrderTable orderTable = new OrderTable(10, false);
        Order order = new Order(orderTable.getId(), new OrderLineItem(1L, 10));

        //when
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //then
        assertThatThrownBy(
                () -> order.changeOrderStatus(OrderStatus.MEAL)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}

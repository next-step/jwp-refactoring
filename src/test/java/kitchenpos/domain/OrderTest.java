package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문에 주문 항목 추가")
    void addOrderLineTest() {
        //given
        OrderTable orderTable = new OrderTable(1L, 10, false);
        Order order = new Order(orderTable.getId());

        //when
        order.addOrderLineItems(Arrays.asList(new OrderLineItem(1L, 10)));

        //then
        assertThat(order.getOrderLineItems()).hasSize(1);
    }

    @Test
    @DisplayName("주문에 빈주문 항목 추가")
    void addEmptyOrderLineTest() {
        //given
        OrderTable orderTable = new OrderTable(1L, 10, false);
        Order order = new Order(orderTable.getId());

        //when & then
        assertThatThrownBy(
                () -> order.addOrderLineItems(Collections.emptyList())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("완료 상태의 주문의 상태 수정")
    void changeOrderStatusTest() {
        //given
        OrderTable orderTable = new OrderTable(1L, 10, false);
        Order order = new Order(orderTable.getId());

        //when
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //then
        assertThatThrownBy(
                () -> order.changeOrderStatus(OrderStatus.MEAL)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}

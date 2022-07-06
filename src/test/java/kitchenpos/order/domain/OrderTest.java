package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Order Domain Test")
class OrderTest {
    @Test
    void changeOrderStatusTest() {
        // given
        Order order = 주문_생성();

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    void validIsNotCompletionTest() {
        // given
        Order order = 주문_생성();

        // then
        assertThatThrownBy(() -> {
            order.changeOrderStatus(OrderStatus.COMPLETION);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    public static Order 주문_생성() {
        return Order.of(1L, 주문_항목_리스트_생성());
    }

    public static List<OrderLineItem> 주문_항목_리스트_생성() {
        List<OrderLineItem> list = new ArrayList<>();
        list.add(OrderLineItem.of(1L, 1L));
        list.add(OrderLineItem.of(2L, 1L));
        return list;
    }
}

package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderException;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    @DisplayName("주문에 주문항목을 추가할경우 주문에서 확인할 수 있음")
    @Test
    public void returnOrderTable() {
        List<OrderLineItem> orderLineItems = Arrays.asList(
                OrderLineItem.builder().seq(1l).build(),
                OrderLineItem.builder().seq(2l).build()
        );
        Order order = Order.builder().orderTableId(1l).orderStatus(OrderStatus.COMPLETION).build();

        order.addOrderLineItems(orderLineItems);

        assertThat(order.getOrderLineItems()).containsAll(orderLineItems);
    }

    @DisplayName("주문상태를 수정할경우 주문상태가 계산완료일 경우 예외발생")
    @Test
    public void throwsExceptionWhenStatusIsComplete() {
        Order order = Order.builder().orderTableId(1l).orderStatus(OrderStatus.COMPLETION).build();

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining("계산이 완료된 주문은 상태를 변경할 수 없습니다");
    }

    @DisplayName("주문상태를 수정할경우 주문상태가 변경")
    @Test
    public void returnOrderStatus() {
        Order order = Order.builder().orderTableId(1l).orderStatus(OrderStatus.COOKING).build();

        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }
}

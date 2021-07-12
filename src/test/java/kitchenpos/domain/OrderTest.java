package kitchenpos.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderStatusCompleteException;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    @DisplayName("주문의 상태를 바꾸려고 할때 이미 계산 완료된 주문")
    @Test
    void 주문상태를_바꾸려고_할때_이미_계산_완료된_주문() {
        //given
        Order order = new Order(1L, new OrderTable(), OrderStatus.COMPLETION);

        //when, then
        assertThatThrownBy(order::validateOrderStatusComplete)
                .isInstanceOf(OrderStatusCompleteException.class);
    }
}

package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {
    @DisplayName("주문상태 변경 예외 - 주문상태가 계산완료인 경우")
    @Test
    public void 주문상태가계산완료인경우_주문상태변경_예외() throws Exception {
        //given
        OrderLineItem orderLineItem = new OrderLineItem(1L, null, 1L, 2L);
        Order order = new Order(1L, 1L, OrderStatus.COOKING, Arrays.asList(orderLineItem));

        //when
        //then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COMPLETION))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

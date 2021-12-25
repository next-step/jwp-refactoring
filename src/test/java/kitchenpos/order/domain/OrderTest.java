package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {
    @DisplayName("주문을 생성한다")
    @Test
    void testCreate() {
        // given
        OrderValidator orderValidator = Mockito.mock(OrderValidator.class);

        // when
        Order order = Order.create(1L, orderValidator);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }
}

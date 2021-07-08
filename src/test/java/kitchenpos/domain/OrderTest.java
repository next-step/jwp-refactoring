package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class OrderTest {
    @Test
    void given_CompletedOrder_when_ChangeOrderStatus_then_ThrowException() {
        // given
        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        // when
        final Throwable throwable = catchThrowable(() -> order.changeStatus(OrderStatus.COMPLETION));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}

package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.OrderTable;

public class OrderTest {
    @Test
    void given_CompletedOrder_when_ChangeOrderStatus_then_ThrowException() {
        // given
        final OrderTable orderTable = new OrderTable();
        final Order order = new Order(orderTable.getId(), OrderStatus.COMPLETION);

        // when
        final Throwable throwable = catchThrowable(() -> order.changeStatus(OrderStatus.COMPLETION));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}

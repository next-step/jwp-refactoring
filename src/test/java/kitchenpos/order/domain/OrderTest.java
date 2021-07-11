package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class OrderTest {
    @Test
    void given_CompletedOrder_when_ChangeOrderStatus_then_ThrowException() {
        // given
        final Order order = new Order(new OrderTable(), OrderStatus.COMPLETION, Collections.emptyList());

        // when
        final Throwable throwable = catchThrowable(() -> order.changeStatus(OrderStatus.COMPLETION));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void given_EmptyOrderTable_when_CreateOrder_then_ThrowException() {
        // given
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(), new OrderLineItem());
        final OrderTable orderTable = new OrderTable(1);
        orderTable.changeEmpty(true);

        // when
        final Throwable throwable = catchThrowable(() -> new Order(orderTable, new OrderLineItems(orderLineItems)));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}

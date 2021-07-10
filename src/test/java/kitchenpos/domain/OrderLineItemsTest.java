package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class OrderLineItemsTest {

    @Test
    void given_EmptyOrderLineItemList_when_CreateOrderLineItems_then_ThrowException() {
        // given
        List<OrderLineItem> orderLineItems = Collections.emptyList();

        // when
        final Throwable throwable = catchThrowable(() -> new OrderLineItems(orderLineItems));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ids() {
        // given
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        List<OrderLineItem> orderLineItemList = Arrays.asList(orderLineItem1, orderLineItem2);
        final OrderLineItems orderLineItems = new OrderLineItems(orderLineItemList);

        // when
        List<Long> actual = orderLineItems.menuIds();

        // then
        assertThat(actual).containsExactly(1L, 2L);
    }
}

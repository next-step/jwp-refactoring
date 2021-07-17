package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;

public class OrderLineItemsTest {

    @Test
    void given_EmptyOrderLineItemList_when_CreateOrderLineItems_then_ThrowException() {
        // given
        final List<OrderLineItem> orderLineItems = Collections.emptyList();

        // when
        final Throwable throwable = catchThrowable(() -> new OrderLineItems(orderLineItems));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ids() {
        // given
        final Order order = mock(Order.class);
        final Menu menu1 = mock(Menu.class);
        final Menu menu2 = mock(Menu.class);
        final OrderLineItem orderLineItem1 = new OrderLineItem(order, menu1.getId(), 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(order, menu2.getId(), 1);
        final List<OrderLineItem> orderLineItemList = Arrays.asList(orderLineItem1, orderLineItem2);
        final OrderLineItems orderLineItems = new OrderLineItems(orderLineItemList);
        given(menu1.getId()).willReturn(1L);
        given(menu2.getId()).willReturn(2L);

        // when
        final List<Long> actual = orderLineItems.menuIds();

        // then
        assertThat(actual).containsExactly(1L, 2L);
    }
}

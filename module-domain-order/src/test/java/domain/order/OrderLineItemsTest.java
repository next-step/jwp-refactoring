package domain.order;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemsTest {

    @Test
    void create() {
        //given
        OrderLineItem orderLineItem1 = OrderLineItem.of(1L, 1);
        OrderLineItem orderLineItem2 = OrderLineItem.of(2L, 2);

        //when
        OrderLineItems orderLineItems = OrderLineItems.of(Lists.list(orderLineItem1, orderLineItem2));

        //then
        assertThat(orderLineItems.getUnmodifiableList()).contains(orderLineItem1, orderLineItem2);
    }

    @Test
    void getMenuIds() {
        //given
        OrderLineItem orderLineItem1 = OrderLineItem.of(1L, 1);
        OrderLineItem orderLineItem2 = OrderLineItem.of(2L, 2);

        //when
        OrderLineItems orderLineItems = OrderLineItems.of(Lists.list(orderLineItem1, orderLineItem2));

        //then
        assertThat(orderLineItems.getMenuIds()).contains(1L, 2L);
    }
}

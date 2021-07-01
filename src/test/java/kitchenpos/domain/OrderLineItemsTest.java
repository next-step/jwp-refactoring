package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemsTest {

    @Test
    void size() {
        // given
        List<OrderLineItem> orderLineItemList = Arrays.asList(new OrderLineItem(), new OrderLineItem(), new OrderLineItem());
        OrderLineItems orderLineItems = new OrderLineItems(orderLineItemList);

        // when
        int size = orderLineItems.size();

        // then
        assertThat(size).isEqualTo(orderLineItemList.size());
    }
}
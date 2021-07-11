package kitchenpos.order.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemsTest {

    @Test
    void add() {
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.add(new OrderLineItem());

        assertThat(orderLineItems.getOrderLineItems()).isNotEmpty();
    }
}

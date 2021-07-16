package kitchenpos.order.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemTest {

    @Test
    void create() {
        OrderLineItem orderLineItem = new OrderLineItem();

        assertThat(orderLineItem).isNotNull();
    }
}

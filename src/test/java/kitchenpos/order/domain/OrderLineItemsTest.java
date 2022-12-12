package kitchenpos.order.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemsTest {

    @Test
    void checkValid() {
        OrderLineItems orderLineItems = OrderLineItems.of(null);

        assertThatThrownBy(orderLineItems::checkNotEmpty)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
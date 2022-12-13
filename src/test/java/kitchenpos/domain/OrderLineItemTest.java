package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static kitchenpos.fixture.OrderLineItemTestFixture.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderLineItemTest {

    @Test
    void of() {
        // given
        Order order = Order.of(
                OrderTable.of(1L, null, 10, false),
                Collections.singletonList(createOrderLineItem(null, 1L, 1))
        );
        long expectedMenuId = 1L;
        long expectedQuantity = 1L;
        OrderLineItem orderLineItem = OrderLineItem.of(order, expectedMenuId, expectedQuantity);

        // when & then
        assertAll(
                () -> assertThat(orderLineItem.getMenuId()).isEqualTo(expectedMenuId),
                () -> assertThat(orderLineItem.getQuantity()).isEqualTo(expectedQuantity)
        );
    }
}

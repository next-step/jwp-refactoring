package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderLineItemTest {

    @Test
    void of() {
        // given
        long expectedMenuId = 1L;
        long expectedQuantity = 1L;
        OrderLineItem orderLineItem = OrderLineItem.of(expectedMenuId, expectedQuantity);

        // when & then
        assertAll(
                () -> assertThat(orderLineItem.getMenuId()).isEqualTo(expectedMenuId),
                () -> assertThat(orderLineItem.getQuantity()).isEqualTo(expectedQuantity)
        );
    }
}

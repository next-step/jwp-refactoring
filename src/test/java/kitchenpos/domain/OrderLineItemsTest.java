package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderLineItemsTest {

    @Test
    void size() {
        // given
        OrderLineItems orderLineItems = new OrderLineItems(
                Arrays.asList(
                        new OrderLineItem(),
                        new OrderLineItem(),
                        new OrderLineItem()
                )
        );

        // when
        int size = orderLineItems.size();

        // then
        assertThat(size)
                .isEqualTo(3);
    }
}
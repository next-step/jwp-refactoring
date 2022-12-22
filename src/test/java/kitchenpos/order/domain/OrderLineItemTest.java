package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class OrderLineItemTest {
    @Test
    void 생성() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, null, 1L, 1);

        assertAll(
                () -> assertThat(orderLineItem.getSeq()).isEqualTo(1L),
                () -> assertThat(orderLineItem.getMenuId()).isEqualTo(1L),
                () -> assertThat(orderLineItem.getQuantity()).isEqualTo(1)
        );
    }
}

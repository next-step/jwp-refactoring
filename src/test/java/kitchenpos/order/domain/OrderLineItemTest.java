package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @Test
    @DisplayName("주문항목 객체가 같은지 검증")
    void verifyEqualsOrderLineItem() {
        final Orders order = new Orders(null, null, null, null, null);
        final OrderLineItem orderLineItem = new OrderLineItem(1L, order, 1L, 1L);

        assertThat(orderLineItem).isEqualTo(new OrderLineItem(1L, order, 1L, 1L));
    }
}

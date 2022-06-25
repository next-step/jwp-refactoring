package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @Test
    @DisplayName("주문항목 객체가 같은지 검증")
    void verifyEqualsOrderLineItem() {
        final OrdersV2 orders = new OrdersV2(null, null, null, null, null);
        final OrderLineItemV2 orderLineItem = new OrderLineItemV2(1L, orders, 1L, 1L);

        assertThat(orderLineItem).isEqualTo(new OrderLineItemV2(1L, orders, 1L, 1L));
    }
}

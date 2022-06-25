package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문 객체가 같은지 검증")
    void verifyEqualsOrder() {
        final OrdersV2 order = new OrdersV2(1L, 1L, OrderStatusV2.COOKING, null, null);

        assertThat(order).isEqualTo(new OrdersV2(1L, 1L, OrderStatusV2.COOKING, null, null));
    }
}

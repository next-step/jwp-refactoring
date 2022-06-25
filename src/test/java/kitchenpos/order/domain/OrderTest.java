package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문 객체가 같은지 검증")
    void verifyEqualsOrder() {
        final Order order = new Order(1L, 1L, OrderStatus.COOKING, null, null);

        assertThat(order).isEqualTo(new Order(1L, 1L, OrderStatus.COOKING, null, null));
    }
}

package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문 객체가 같은지 검증")
    void verifyEqualsOrder() {
        final Orders order = new Orders.Builder(1L)
                .setId(1L)
                .setOrderStatus(OrderStatus.COOKING)
                .build();

        assertThat(order).isEqualTo(new Orders.Builder(1L)
                .setId(1L)
                .setOrderStatus(OrderStatus.COOKING)
                .build());
    }
}

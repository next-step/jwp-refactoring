package kitchenpos.domain;

import kitchenpos.domain.order.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@DisplayName("주문 테스트")
class OrderTest {

    @DisplayName("id가 같은 두 객체는 같다.")
    @Test
    void equalsTest() {
        Order order1 = Order.of(1L, 1L, Collections.emptyList());
        Order order2 = Order.of(1L, 1L, Collections.emptyList());

        Assertions.assertThat(order1).isEqualTo(order2);
    }

    @DisplayName("id가 다르면 두 객체는 다르다.")
    @Test
    void equalsTest2() {
        Order order1 = Order.of(1L, 1L, Collections.emptyList());
        Order order2 = Order.of(2L, 1L, Collections.emptyList());

        Assertions.assertThat(order1).isNotEqualTo(order2);
    }
}

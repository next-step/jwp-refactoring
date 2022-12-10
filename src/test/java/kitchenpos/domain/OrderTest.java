package kitchenpos.domain;

import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테스트")
class OrderTest {

    @DisplayName("id가 같은 두 객체는 동등하다.")
    @Test
    void equalsTest() {
        Order order1 = new Order(1L, 1L, Collections.emptyList());
        Order order2 = new Order(1L, 1L, Collections.emptyList());

        Assertions.assertThat(order1).isEqualTo(order2);
    }

    @DisplayName("id가 다르면 두 객체는 동등하지 않다.")
    @Test
    void equalsTest2() {
        Order order1 = new Order(1L, 1L, Collections.emptyList());
        Order order2 = new Order(2L, 1L, Collections.emptyList());

        Assertions.assertThat(order1).isNotEqualTo(order2);
    }
}

package kitchenpos.order.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 메뉴 테스트")
class OrderMenuTest {

    @DisplayName("주문 메뉴 id가 다른 객체는 동등하지 않다.")
    @Test
    void equalsTest() {
        OrderMenu menu1 = OrderMenu.from(1L);
        OrderMenu menu2 = OrderMenu.from(2L);

        Assertions.assertThat(menu1).isNotEqualTo(menu2);
    }

    @DisplayName("주문 메뉴 id가 같은 객체는 동등하다.")
    @Test
    void equalsTest2() {
        OrderMenu menu1 = OrderMenu.from(1L);
        OrderMenu menu2 = OrderMenu.from(1L);

        Assertions.assertThat(menu1).isEqualTo(menu2);
    }
}

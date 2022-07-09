package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 항목")
class OrderLineItemTest {

    @DisplayName("같은 메뉴를 가졌는지 확인할 수 있다.")
    @Test
    void 같은_메뉴를_가졌는지_확인() {
        Long 메뉴_아이디 = 1L;
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴_아이디, 1L);
        OrderLineItem 같은_메뉴의_주문_항목2 = OrderLineItem.of(메뉴_아이디, 2L);

        assertThat(주문_항목.hasSameMenu(같은_메뉴의_주문_항목2)).isTrue();
    }
}

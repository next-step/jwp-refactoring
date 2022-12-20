package kitchenpos.order.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@DisplayName("주문 항목 테스트")
class OrderLineItemTest {

    private OrderMenu menu = OrderMenu.of(1L, "허니콤보치킨", BigDecimal.valueOf(18000));

    @DisplayName("id가 같은 두 객체는 같다.")
    @Test
    void equalsTest() {
        OrderLineItem orderLineItem1 = OrderLineItem.of(1L, menu, 2);
        OrderLineItem orderLineItem2 = OrderLineItem.of(1L, menu, 2);

        Assertions.assertThat(orderLineItem1).isEqualTo(orderLineItem2);
    }

    @DisplayName("id가 다르면 두 객체는 다르다.")
    @Test
    void equalsTest2() {
        OrderLineItem orderLineItem1 = OrderLineItem.of(1L, menu, 2);
        OrderLineItem orderLineItem2 = OrderLineItem.of(2L, menu, 2);

        Assertions.assertThat(orderLineItem1).isNotEqualTo(orderLineItem2);
    }
}

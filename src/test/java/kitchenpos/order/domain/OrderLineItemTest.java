package kitchenpos.order.domain;

import kitchenpos.order.domain.OrderLineItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 항목 테스트")
class OrderLineItemTest {

    @DisplayName("id가 같은 두 객체는 동등하다.")
    @Test
    void equalsTest() {
        OrderLineItem orderLineItem1 = OrderLineItem.of(1L, 1L, 2);
        OrderLineItem orderLineItem2 = OrderLineItem.of(1L, 1L, 2);

        Assertions.assertThat(orderLineItem1).isEqualTo(orderLineItem2);
    }

    @DisplayName("id가 다르면 두 객체는 동등하지 않다.")
    @Test
    void equalsTest2() {
        OrderLineItem orderLineItem1 = OrderLineItem.of(1L, 1L, 2);
        OrderLineItem orderLineItem2 = OrderLineItem.of(2L, 1L, 2);

        Assertions.assertThat(orderLineItem1).isNotEqualTo(orderLineItem2);
    }
}

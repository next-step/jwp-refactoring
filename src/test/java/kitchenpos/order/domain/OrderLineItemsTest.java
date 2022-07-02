package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemsTest {

    @Test
    @DisplayName("orderLineItem 이 없으면 true 를 반환한다")
    void isEmptyFalse() {
        // when
        OrderLineItems orderLineItems = new OrderLineItems();

        // then
        assertThat(orderLineItems.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("orderLineItem 이 존재하면 flase 를 반환한다")
    void isEmptyTrue() {
        // when
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.add(new OrderLineItem(new Order(), 1L, 10L));

        // then
        assertThat(orderLineItems.isEmpty()).isFalse();
    }
}

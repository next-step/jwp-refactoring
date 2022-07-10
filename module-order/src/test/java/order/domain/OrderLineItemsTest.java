package order.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderLineItemsTest {

    @Test
    void 주문_목록을_추가한다() {
        Order order = new Order(1L);
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.addAll(order, Collections.singletonList(new OrderLineItem(1L, 1)));

        assertAll(
                () -> assertThat(orderLineItems.get()).hasSize(1),
                () -> assertThat(orderLineItems.get().get(0).getOrder()).isEqualTo(order)
        );
    }

    @Test
    void 빈_주문_목록을_추가하는_경우() {
        Order order = new Order(1L);
        OrderLineItems orderLineItems = new OrderLineItems();

        assertThatThrownBy(() -> orderLineItems.addAll(order, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

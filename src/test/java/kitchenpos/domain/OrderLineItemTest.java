package kitchenpos.domain;

import static kitchenpos.fixture.OrderFactory.createOrder;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @Test
    void 주문_연결() {

        // given
        OrderLineItem orderLineItem = new OrderLineItem(1L, 5);
        Order order = createOrder(1L, new OrderTable(NumberOfGuests.from(5), false), "COMPLETION", null,
                Arrays.asList(orderLineItem));

        // when
        orderLineItem.connectTo(order);

        // then
        assertThat(orderLineItem.getOrder().getId()).isEqualTo(order.getId());
    }
}

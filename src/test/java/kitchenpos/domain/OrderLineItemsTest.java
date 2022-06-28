package kitchenpos.domain;

import static kitchenpos.fixture.DomainFactory.createOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @Test
    void 존재하지_않는_메뉴_예외() {

        // given
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem());

        // when, then
        assertThatThrownBy(
                () -> OrderLineItems.of(orderLineItems, 0)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_연결() {
        // given
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(), new OrderLineItem());

        // when
        Order order = createOrder(1L, new OrderTable(NumberOfGuests.from(5), false), "COMPLETION", null,
                orderLineItems);
        OrderLineItems.from(orderLineItems).connectToOrder(order);

        // then
        assertAll(
                () -> assertThat(orderLineItems.get(0).getOrder().getId()).isEqualTo(order.getId()),
                () -> assertThat(orderLineItems.get(1).getOrder().getId()).isEqualTo(order.getId())
        );
    }

    @Test
    void 주문_항목_목록_없을_경우_예외() {
        // when, then
        assertThatThrownBy(
                () -> OrderLineItems.from(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목_목록_비었을_경우_예외() {
        // when, then
        assertThatThrownBy(
                () -> OrderLineItems.from(Arrays.asList())
        ).isInstanceOf(IllegalArgumentException.class);
    }

}

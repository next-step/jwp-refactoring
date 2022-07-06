package kitchenpos.order.domain;

import static kitchenpos.fixture.OrderFactory.createOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import kitchenpos.exception.EmptyOrderLineItemsException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {
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
        ).isInstanceOf(EmptyOrderLineItemsException.class);
    }

    @Test
    void 주문_항목_목록_비었을_경우_예외() {
        // when, then
        assertThatThrownBy(
                () -> OrderLineItems.from(Arrays.asList())
        ).isInstanceOf(EmptyOrderLineItemsException.class);
    }

}

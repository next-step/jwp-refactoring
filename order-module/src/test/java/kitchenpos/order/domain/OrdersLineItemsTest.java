package kitchenpos.order.domain;

import kitchenpos.order.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 항목 일급 컬렉션 테스트")
class OrdersLineItemsTest {

    private List<OrderLineItem> 주문_항목;
    private Orders orders;

    @BeforeEach
    void setUp() {
        orders = new Orders(1L, 1L, OrderStatus.COOKING, LocalDateTime.now());
        주문_항목 = Arrays.asList(
                new OrderLineItem(1L, orders, 1L, 1),
                new OrderLineItem(2L, orders, 2L, 1)
        );
    }

    @Test
    void 주문_항목_추가() {
        OrderLineItem orderLineItem = new OrderLineItem(3L, orders, 1L, 1);
        OrderLineItems orderLineItems = new OrderLineItems(주문_항목);
        orderLineItems.addOrderLineItem(orderLineItem);
        ArrayList<OrderLineItem> expected = new ArrayList<>(주문_항목);
        expected.add(orderLineItem);
        assertThat(orderLineItems.orderLineItems()).containsExactlyElementsOf(expected);
    }
}

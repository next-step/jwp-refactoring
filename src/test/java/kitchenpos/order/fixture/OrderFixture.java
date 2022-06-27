package kitchenpos.order.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.order.fixture.OrderLineItemFixture.주문_항목;

public class OrderFixture {

    public static Order 요리중_상태_주문 = create(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(주문_항목));
    public static Order 주문완료_상태_주문 = create(2L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList(주문_항목));

    public static Order create(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);

        return order;
    }
}

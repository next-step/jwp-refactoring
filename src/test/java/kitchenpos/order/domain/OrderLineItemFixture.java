package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.test.util.ReflectionTestUtils;

public class OrderLineItemFixture {

    public static OrderLineItem 주문라인아이템(Long seq, Order order, Menu menu, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem(order, menu, quantity);
        ReflectionTestUtils.setField(orderLineItem, "seq", seq);
        return orderLineItem;
    }
}

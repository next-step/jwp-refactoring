package kitchenpos.domain;

import kitchenpos.domin.Order;
import kitchenpos.domin.OrderLineItem;
import org.springframework.test.util.ReflectionTestUtils;

public class OrderLineItemFixture {

    public static OrderLineItem 주문라인아이템(Long seq, Order order, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem(order, menuId, quantity);
        ReflectionTestUtils.setField(orderLineItem, "seq", seq);
        return orderLineItem;
    }
}

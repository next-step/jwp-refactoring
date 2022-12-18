package kitchenpos.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class OrderLineItemFixture {

    public static OrderLineItem 주문라인아이템(Long seq, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem(menuId, quantity);
        ReflectionTestUtils.setField(orderLineItem, "seq", seq);
        return orderLineItem;
    }
}

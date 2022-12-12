package kitchenpos.domain;

import static kitchenpos.domain.ProductTestFixture.*;

public class OrderLineItemTestFixture {
    public static final OrderLineItem 짜장면_1그릇 = orderLineItem(1L, 짜장면.id(), 1);
    public static final OrderLineItem 짬뽕_2그릇 = orderLineItem(2L, 짬뽕.id(), 2);

    public static OrderLineItem orderLineItem(Long seq, Long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(null);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}

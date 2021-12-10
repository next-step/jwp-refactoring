package kitchenpos.application.sample;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemSample {

    public static OrderLineItem twoOrderItem() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setQuantity(2);
        orderLineItem.setMenuId(1L);
        return orderLineItem;
    }
}

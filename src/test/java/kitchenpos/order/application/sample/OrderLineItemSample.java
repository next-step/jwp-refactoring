package kitchenpos.order.application.sample;

import static kitchenpos.menu.application.sample.MenuSample.후라이드치킨세트;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemSample {

    public static OrderLineItem 후라이드치킨세트_두개() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setQuantity(2);
        orderLineItem.setMenuId(후라이드치킨세트().getId());
        return orderLineItem;
    }
}

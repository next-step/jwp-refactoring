package kitchenpos.utils.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemObjects {
    private final MenuObjects menuObjects;
    private final OrderLineItem orderLineItem1;
    private final OrderLineItem orderLineItem2;
    private final OrderLineItem orderLineItem3;
    private final OrderLineItem orderLineItem4;

    public OrderLineItemObjects() {
        menuObjects = new MenuObjects();
        orderLineItem1 = new OrderLineItem();
        orderLineItem2 = new OrderLineItem();
        orderLineItem3 = new OrderLineItem();
        orderLineItem4 = new OrderLineItem();

        orderLineItem1.setSeq(1L);
        orderLineItem1.setMenuId(menuObjects.getMenu1().getId());

        orderLineItem2.setSeq(2L);
        orderLineItem2.setMenuId(menuObjects.getMenu2().getId());

        orderLineItem3.setSeq(3L);
        orderLineItem3.setMenuId(menuObjects.getMenu3().getId());

        orderLineItem4.setSeq(4L);
        orderLineItem4.setMenuId(menuObjects.getMenu4().getId());
    }

    public OrderLineItem getOrderLineItem1() {
        return orderLineItem1;
    }

    public OrderLineItem getOrderLineItem2() {
        return orderLineItem2;
    }

    public OrderLineItem getOrderLineItem3() {
        return orderLineItem3;
    }

    public OrderLineItem getOrderLineItem4() {
        return orderLineItem4;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return new ArrayList<>(Arrays.asList(orderLineItem1, orderLineItem2, orderLineItem3, orderLineItem4));
    }
}

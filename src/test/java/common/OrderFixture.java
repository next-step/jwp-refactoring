package common;

import static java.util.Arrays.asList;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixture {

    public static Order 주문_첫번째() {

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);

        Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);
        order.setOrderStatus(COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(asList(orderLineItem));

        return order;
    }

    public static Order 주문_첫번째_완료() {


        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);

        Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);
        order.setOrderStatus(COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(asList(orderLineItem));

        return order;
    }

    public static Order 주문_두번째() {

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(2L);
        orderLineItem.setMenuId(2L);
        orderLineItem.setQuantity(1L);

        Order order = new Order();
        order.setId(2L);
        order.setOrderTableId(2L);
        order.setOrderStatus(COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(asList(orderLineItem));

        return order;
    }
}

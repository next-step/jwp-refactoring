package common;

import static common.MenuFixture.*;
import static java.util.Arrays.asList;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class OrderFixture {

    public static Order 주문() {
        return Order.createCook(1L, OrderTableFixture.첫번째_주문테이블(),
            asList(OrderLineItem.of(1L, 메뉴_양념치킨(), 1L)));
    }

    public static Order 주문_완료() {
        Order order = Order.createCook(1L, OrderTableFixture.첫번째_주문테이블(),
            asList(OrderLineItem.of(1L, 메뉴_양념치킨(), 1L)));
        order.changeOrderStatus(OrderStatus.COMPLETION.name());
        return order;
    }
}

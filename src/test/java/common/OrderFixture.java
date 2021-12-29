package common;

import static common.MenuFixture.메뉴_양념치킨;
import static java.util.Arrays.asList;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class OrderFixture {

    public static Order 주문() {
        Menu 메뉴_양념치킨 = 메뉴_양념치킨();
        return Order.createCook(1L,
            asList(OrderLineItem.of(1L, 메뉴_양념치킨.getId(), 1L)));
    }

    public static Order 계산_완료() {
        Menu 메뉴_양념치킨 = 메뉴_양념치킨();
        Order order = Order.createCook(1L,
            asList(OrderLineItem.of(1L, 메뉴_양념치킨.getId(), 1L)));
        order.changeOrderStatus(OrderStatus.COMPLETION.name());
        return order;
    }

}

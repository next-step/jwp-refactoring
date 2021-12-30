package kitchenpos.moduledomain.common;


import static java.util.Arrays.asList;

import kitchenpos.moduledomain.menu.Menu;
import kitchenpos.moduledomain.order.Order;
import kitchenpos.moduledomain.order.OrderLineItem;
import kitchenpos.moduledomain.order.OrderStatus;

public class OrderFixture {

    public static Order 주문() {
        Menu 메뉴_양념치킨 = MenuFixture.메뉴_양념치킨();
        return Order.createCook(1L,
            asList(OrderLineItem.of(1L, 메뉴_양념치킨.getId(), 1L)));
    }

    public static Order 계산_완료() {
        Menu 메뉴_양념치킨 = MenuFixture.메뉴_양념치킨();
        Order order = Order.createCook(1L,
            asList(OrderLineItem.of(1L, 메뉴_양념치킨.getId(), 1L)));
        order.changeOrderStatus(OrderStatus.COMPLETION.name());
        return order;
    }

}

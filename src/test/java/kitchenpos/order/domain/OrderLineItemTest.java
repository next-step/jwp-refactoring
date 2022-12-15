package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemTest {

    public static OrderLineItem 주문_항목_생성(Long seq, Order order, Menu menu, long quantity) {
        return new OrderLineItem.Builder()
                .seq(seq)
                .order(order)
                .menu(menu)
                .quantity(quantity)
                .build();
    }
}
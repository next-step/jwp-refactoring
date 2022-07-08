package kitchenpos.factory;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.table.OrderTable;

import java.math.BigDecimal;

public class OrderFixture {
    public static OrderTable 주문테이블_생성(Long id) {
        return new OrderTable(id, 2, false);
    }

    public static OrderTable 주문테이블_생성(Long id, int numberOfGuests, boolean isEmpty) {
        return new OrderTable(id, numberOfGuests, isEmpty);
    }

    public static OrderLineItem 주문_메뉴_생성(Long seq, Order order, Long menuId, long quantity) {
        return new OrderLineItem(seq, order, menuId, quantity);
    }

    public static Menu 메뉴생성(Long id, String name, BigDecimal price, Long menuGroupId) {
        return new Menu(id, name, price, menuGroupId);
    }
}

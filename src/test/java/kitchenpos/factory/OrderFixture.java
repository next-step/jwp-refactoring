package kitchenpos.factory;

import kitchenpos.domain.*;

import java.math.BigDecimal;

public class OrderFixture {
    public static OrderTable 주문테이블_생성(Long id) {
        return new OrderTable(id, 2, false);
    }

    public static OrderTable 주문테이블_생성(Long id, int numberOfGuests, boolean isEmpty) {
        return new OrderTable(id, numberOfGuests, isEmpty);
    }

    public static OrderLineItem 주문_메뉴_생성(Long seq, Order order, Menu menu, Long menuId) {
        return new OrderLineItem(seq, order, menu, menuId);
    }

    public static Menu 메뉴생성(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(id, name, price, menuGroup);
    }
}

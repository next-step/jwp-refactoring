package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

public class OrderMenuTestFixture {

    public static OrderMenu generateOrderMenu(Menu menu) {
        return OrderMenu.from(menu);
    }
}

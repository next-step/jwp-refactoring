package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

public class OrderMenuTestFixture {

    public static OrderMenu create(Menu menu) {
        return OrderMenu.from(menu);
    }
}

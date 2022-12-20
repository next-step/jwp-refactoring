package kitchenpos.order.testfixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderMenu;

public class OrderMenuTestFixture {

    public static OrderMenu create(Menu menu) {
        return OrderMenu.from(menu);
    }
}

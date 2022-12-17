package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

public class OrderMenuTestFixture {

    public static OrderMenu generateOrderMenu(Menu menu) {
        return new OrderMenu(menu.getId(), menu.getName(), menu.getPrice());
    }
}

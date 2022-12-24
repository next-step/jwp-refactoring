package kitchenpos.fixture;

import java.math.BigDecimal;

import kitchenpos.menu.domain.OrderMenu;

public class OrderMenuFixture {
    private OrderMenuFixture() {
    }

    public static OrderMenu savedOrderMenu(Long id, Long menuId, String name, BigDecimal price) {
        return OrderMenu.of(id, menuId, name, price);
    }
}

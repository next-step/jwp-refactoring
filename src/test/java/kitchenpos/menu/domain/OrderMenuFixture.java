package kitchenpos.menu.domain;

import java.math.BigDecimal;

public class OrderMenuFixture {
    private OrderMenuFixture() {
    }

    public static OrderMenu savedOrderMenu(Long id, Long menuId, String name, BigDecimal price) {
        return OrderMenu.of(id, menuId, name, price);
    }
}

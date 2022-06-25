package kitchenpos.factory;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;

public class MenuFixtureFactory {
    public static Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId) {
        return new Menu(id, name, price, menuGroupId);
    }

    public static Menu createMenu(String name, BigDecimal price, Long menuGroupId) {
        return new Menu(name, price, menuGroupId);
    }
}

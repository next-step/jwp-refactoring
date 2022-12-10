package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuFixture {
    public static Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    public static Menu createMenu(String name, BigDecimal price, Long menuGroupId) {
        return new Menu(null, name, price, menuGroupId, new ArrayList<>());
    }

    public static Menu createMenu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }
}

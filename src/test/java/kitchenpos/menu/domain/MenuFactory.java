package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuFactory {
    public static Menu create(long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }
}

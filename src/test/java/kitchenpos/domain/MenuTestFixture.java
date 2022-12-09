package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuTestFixture {
    public static Menu menu(Long id, String name, Long menuGroupId, BigDecimal price, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setMenuGroupId(menuGroupId);
        menu.setPrice(price);
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}

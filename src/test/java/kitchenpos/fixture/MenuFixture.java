package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Arrays;

public class MenuFixture {

    public static Menu 메뉴(long menuGroupId, String name, BigDecimal price, MenuProduct menuProduct) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(Arrays.asList(menuProduct));
        return menu;
    }

    public static Menu 메뉴(MenuGroup menuGroup, String name, BigDecimal price, MenuProduct menuProduct) {
        return 메뉴(menuGroup.getId(), name, price, menuProduct);
    }
}

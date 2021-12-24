package kitchenpos.common.fixtrue;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Arrays;

public class MenuFixture {

    private MenuFixture() {

    }

    public static Menu of(long id, String name, BigDecimal price, Long menuGroupId, MenuProduct... menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(Arrays.asList(menuProducts));
        return menu;
    }
}

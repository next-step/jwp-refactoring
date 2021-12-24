package kitchenpos.common.fixtrue;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Arrays;

public class MenuFixture {

    private MenuFixture() {

    }

    public static Menu of(long id, String name, BigDecimal price, Long menuGroupId, MenuProduct... menuProducts) {
        return Menu.of(id, name, price, menuGroupId, Arrays.asList(menuProducts));
    }
}

package kitchenpos.common.fixtrue;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;

public class MenuFixture {

    private MenuFixture() {

    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId) {
        return Menu.of(name, price, menuGroupId);
    }
}

package kitchenpos.menu.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductBag;

import java.math.BigDecimal;

public class MenuTestFixture {

    public static Menu 메뉴(String name, BigDecimal price, Long menuGroupId, MenuProductBag menuProducts) {
        return Menu.of(name, price, menuGroupId, menuProducts);
    }
}

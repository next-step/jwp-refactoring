package kitchenpos.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class MenuFixtures {
    public static Menu 메뉴(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static Menu 양념치킨두마리메뉴() {
        return new Menu(
                "양념치킨두마리메뉴",
                new BigDecimal(32000),
                1L,
                Arrays.asList(new MenuProduct(2L, 2L)));
    }
}


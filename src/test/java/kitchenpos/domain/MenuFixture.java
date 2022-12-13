package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {
    private MenuFixture() {
    }

    public static Menu menuParam(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }

    public static Menu savedMenu(Long id, String name, BigDecimal price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }
}

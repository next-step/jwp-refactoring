package kitchenpos.menu.testfixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuTestFixture {

    public static Menu create(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return Menu.of(id, name, price, menuGroup.getId(), menuProducts);
    }

    public static Menu create(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return Menu.of(null, name, price, menuGroup.getId(), menuProducts);
    }
}

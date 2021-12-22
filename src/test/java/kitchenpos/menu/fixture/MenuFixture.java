package kitchenpos.menu.fixture;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

public class MenuFixture {

    private MenuFixture() {
    }

    public static Menu createMenu(final Long id, final String name, final Long price,
        final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }
}

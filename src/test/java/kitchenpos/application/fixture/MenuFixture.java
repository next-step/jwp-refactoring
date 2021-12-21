package kitchenpos.application.fixture;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    private MenuFixture() {
    }

    public static Menu createMenu(final Long id, final String name, final Long price,
        final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }
}

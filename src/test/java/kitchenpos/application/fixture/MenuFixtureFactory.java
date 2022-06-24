package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

public class MenuFixtureFactory {

    private MenuFixtureFactory() {
    }

    public static Menu create(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup) {
        return new Menu(id, name, price, menuGroup);
    }
}

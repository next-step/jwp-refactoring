package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.menu.Menu;

public class MenuFixtureFactory {

    private MenuFixtureFactory() {}

    public static Menu create(String name, BigDecimal price, Long menuGroupId) {
        return Menu.of(name, price, menuGroupId);
    }
}

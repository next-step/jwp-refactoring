package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;

public class MenuFixtureFactory {

    private MenuFixtureFactory() {
    }

    public static Menu create(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        return new Menu(id, name, price, menuGroupId);
    }
}

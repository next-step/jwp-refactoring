package kitchenpos.application.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

public class MenuFixtureFactory {

    private MenuFixtureFactory() {}

    public static Menu create(long id, String name, long price, MenuGroup menuGroup) {
        return Menu.of(id, name, BigDecimal.valueOf(price), menuGroup, new ArrayList<>());
    }
}

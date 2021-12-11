package kitchenpos.application.fixture;

import java.math.BigDecimal;

import kitchenpos.domain.Menu;

public class MenuFixtureFactory {

    private MenuFixtureFactory() {}

    public static Menu create(long id, String name, long price, long menuGroupId) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);

        return menu;
    }
}

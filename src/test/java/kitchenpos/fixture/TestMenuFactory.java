package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.util.diff.myers.Snake;

import java.math.BigDecimal;

public class TestMenuFactory {

    public static Menu create(int price, MenuGroup menuGroup, String name) {
        return create(null, price, menuGroup, name);
    }

    public static Menu create(Long id, int price, MenuGroup menuGroup, String name) {
        return create(id, price, menuGroup.getId(), name);
    }

    public static Menu create(int price, Long menuGroupId, String name) {
        return create(null, price, menuGroupId, name);
    }

    public static Menu create(Long id, int price, Long menuGroupId, String name) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setName(name);

        return menu;
    }
}

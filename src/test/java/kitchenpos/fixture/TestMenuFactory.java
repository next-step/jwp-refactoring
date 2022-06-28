package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestMenuFactory {

    public static Menu create(int price, MenuGroup menuGroup, String name) {
        return create(null, price, menuGroup, name);
    }

    public static Menu create(Long id, int price, MenuGroup menuGroup, String name) {
        return create(id, price, menuGroup.getId(), name);
    }

    public static Menu create(String name) {
        return create(0, 0L, name);
    }

    public static Menu create(int price, Long menuGroupId, String name) {
        return create(null, price, menuGroupId, name);
    }

    public static Menu create(Long id, int price, Long menuGroupId, String name) {
        return create(id, price, menuGroupId, name, new ArrayList<>());
    }

    public static Menu create(Long id, int price, Long menuGroupId, String name, List<MenuProduct> menuProducts) {
        return new Menu(id, name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }
}

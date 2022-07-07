package kitchenpos;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Name;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;


import java.util.List;

public class TestMenuFactory {
    public static Menu create(Long id, int price, Long menuGroupId, String name, List<MenuProduct> menuProducts) {
        return new Menu(id, new Name(name), new Price(price), menuGroupId, new MenuProducts(menuProducts));
    }
}

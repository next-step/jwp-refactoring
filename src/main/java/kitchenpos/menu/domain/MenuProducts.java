package kitchenpos.menu.domain;

import java.util.Collections;
import java.util.List;

public class MenuProducts {

    private final List<MenuProduct> menuProductList;

    private MenuProducts(List<MenuProduct> menuProductList) {
        this.menuProductList = menuProductList;
    }

    public static MenuProducts of(List<MenuProduct> menuProductList) {
        return new MenuProducts(menuProductList);
    }

    public boolean contain(MenuProduct menuProduct) {
        return menuProductList.contains(menuProduct);
    }

    public List<MenuProduct> toCollection() {
        return Collections.unmodifiableList(menuProductList);
    }

}
